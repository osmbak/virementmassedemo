package virement.masse.demo.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import virement.masse.demo.dto.Pain001ProcessResponse;
import virement.masse.demo.generateur.Pacs008ByBankXmlGenerator;
import virement.masse.demo.model.Pacs008GeneratedByBank;
import virement.masse.demo.model.Pain001File;
import virement.masse.demo.model.Pain001Transaction;
import virement.masse.demo.parseur.Pain001StaxParser;
import virement.masse.demo.repository.Pacs008GeneratedByBankRepository;
import virement.masse.demo.repository.Pain001FileRepository;
import virement.masse.demo.service.Pain001ProcessingService;
import virement.masse.demo.utils.Pacs008XsdValidator;

import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class Pain001ProcessingServiceImpl implements Pain001ProcessingService {

	private static final String BANQUE_T24 = "190";
	private static final String Code_BCP= "1";

	private static final String BANQUE_EMETTRICE = "190";

	private final Pain001StaxParser pain001StaxParser;
	private final Pain001FileRepository pain001FileRepository;
	private final Pacs008GeneratedByBankRepository pacs008GeneratedByBankRepository;
	private final Pacs008ByBankXmlGenerator pacs008ByBankXmlGenerator;
	private final Pacs008XsdValidator pacs008XsdValidator;

	@Override
	@Transactional
	public Pain001ProcessResponse processPain001(InputStream inputStream, String fileName) {

		if (inputStream == null) {
			throw new IllegalArgumentException("Le flux pain.001 est vide ou manquant");
		}

		try {
			Pain001File pain001File = pain001StaxParser.parse(inputStream);
			pain001File.setFileName(fileName);

			if (isBlank(pain001File.getMsgId())) {
				pain001File.setStatut("PARSE_KO");
				pain001File.setErrorMessage("MsgId manquant dans le fichier pain.001");

				Pain001File savedError = pain001FileRepository.save(pain001File);

				return new Pain001ProcessResponse(savedError.getMsgId(), savedError.getStatut(),
						savedError.getNbOfTxs(), savedError.getCtrlSum(), 0);
			}

			if (pain001FileRepository.existsByMsgId(pain001File.getMsgId())) {
				throw new IllegalArgumentException("Fichier déjà traité avec MsgId : " + pain001File.getMsgId());
			}

			Pain001File savedFile = pain001FileRepository.save(pain001File);

			int nbPacs = genererPacs008ParBanque(savedFile);

			savedFile.setStatut("GENERATED");
			Pain001File finalFile = pain001FileRepository.save(savedFile);

			return new Pain001ProcessResponse(finalFile.getMsgId(), finalFile.getStatut(), finalFile.getNbOfTxs(),
					finalFile.getCtrlSum(), nbPacs);

		} catch (IllegalArgumentException e) {
			throw e;

		} catch (Exception e) {
			Pain001File errorFile = new Pain001File();
			errorFile.setFileName(fileName);
			errorFile.setStatut("PARSE_KO");
			errorFile.setErrorMessage(e.getMessage());

			Pain001File savedError = pain001FileRepository.save(errorFile);

			return new Pain001ProcessResponse(savedError.getMsgId(), savedError.getStatut(), savedError.getNbOfTxs(),
					savedError.getCtrlSum(), 0);
		}
	}

	private int genererPacs008ParBanque(Pain001File pain001File) {

		if (pain001File == null || pain001File.getTransactions() == null || pain001File.getTransactions().isEmpty()) {
			return 0;
		}

		Map<String, List<Pain001Transaction>> transactionsParBanque = pain001File.getTransactions().stream()
				.filter(tx -> tx != null).filter(tx -> !isBlank(resolveBankCode(tx)))
				.collect(Collectors.groupingBy(this::resolveBankCode));

		System.out.println("Banques détectées = " + transactionsParBanque.keySet());

		int nbPacsGenerated = 0;
		int sequence = 1;

		for (Map.Entry<String, List<Pain001Transaction>> entry : transactionsParBanque.entrySet()) {

			String bankCode = entry.getKey();
			List<Pain001Transaction> transactions = entry.getValue();

			try {
				BigDecimal total = sumAmount(transactions);

				if (bankCode != null && bankCode.startsWith(Code_BCP)) {
					
					String fileName = "t24_" + cleanFileName(pain001File.getMsgId()) + "_" + cleanFileName(bankCode)
							+ ".txt";

					String filePath = generateT24FlatFile(pain001File, bankCode, transactions, fileName);

					Pacs008GeneratedByBank generatedT24 = new Pacs008GeneratedByBank();
					generatedT24.setPain001File(pain001File);
					generatedT24.setOriginalPainMsgId(pain001File.getMsgId());
					generatedT24.setCreditorBankBic(bankCode);
					generatedT24.setGeneratedMsgId("T24-" + pain001File.getMsgId() + "-" + bankCode);
					generatedT24.setNbOfTxs(transactions.size());
					generatedT24.setCtrlSum(total);
					generatedT24.setCurrency(resolveCurrency(transactions));
					generatedT24.setFileName(fileName);
					generatedT24.setFilePath(filePath);
					generatedT24.setStatut("T24_FILE_GENERATED");

					pacs008GeneratedByBankRepository.save(generatedT24);

					System.out.println("Fichier plat T24 généré pour banque " + bankCode + " : " + filePath);

					continue;
				}

				String generatedMsgId = generatePacs008MsgId(BANQUE_EMETTRICE, bankCode, sequence);

				String xml = pacs008ByBankXmlGenerator.generateXml(pain001File, bankCode, transactions, generatedMsgId);

				//10-07-2026 desactivation validation

				pacs008XsdValidator.validate(xml);

				String fileName = "pacs008_" + cleanFileName(pain001File.getMsgId()) + "_" + cleanFileName(bankCode)
						+ ".xml";

				String filePath = saveXmlFile(fileName, xml);

				nbPacsGenerated++;
				sequence++;

				Pacs008GeneratedByBank generated = new Pacs008GeneratedByBank();
				generated.setPain001File(pain001File);
				generated.setOriginalPainMsgId(pain001File.getMsgId());
				generated.setCreditorBankBic(bankCode);
				generated.setGeneratedMsgId(generatedMsgId);
				generated.setNbOfTxs(transactions.size());
				generated.setCtrlSum(total);
				generated.setCurrency(resolveCurrency(transactions));
				generated.setFileName(fileName);
				generated.setFilePath(filePath);
				generated.setStatut("GENERATED");

				pacs008GeneratedByBankRepository.save(generated);

				System.out.println("PACS008 généré pour banque " + bankCode + " avec MsgId : " + generatedMsgId);

			} catch (Exception e) {
				System.out.println("Erreur génération fichier banque " + bankCode + " : " + e.getMessage());
				e.printStackTrace();

				Pacs008GeneratedByBank generatedError = new Pacs008GeneratedByBank();
				generatedError.setPain001File(pain001File);
				generatedError.setOriginalPainMsgId(pain001File.getMsgId());
				generatedError.setCreditorBankBic(bankCode);
				generatedError.setNbOfTxs(transactions != null ? transactions.size() : 0);
				generatedError.setCtrlSum(BigDecimal.ZERO);
				generatedError.setCurrency(resolveCurrency(transactions));
				generatedError.setStatut("ERROR");
				generatedError.setErrorMessage(e.getMessage());

				pacs008GeneratedByBankRepository.save(generatedError);
			}
		}

		System.out.println("Nombre PACS008 générés = " + nbPacsGenerated);
		return nbPacsGenerated;
	}

	private String generatePacs008MsgId(String banqueEmettrice, String banqueBeneficiaire, int sequence) {
		int dayOfYear = LocalDate.now().getDayOfYear();
		int year = LocalDate.now().getYear();

		String quantieme = String.format("%03d", dayOfYear);
		String seq = String.format("%022d", sequence);

		return banqueEmettrice + banqueBeneficiaire + quantieme + year + seq;
	}

	private String generateT24FlatFile(Pain001File pain001File, String bankCode, List<Pain001Transaction> transactions,
			String fileName) {

		try {
			Path outputDir = Paths.get("output/t24");
			Files.createDirectories(outputDir);

			Path filePath = outputDir.resolve(fileName);

			StringBuilder builder = new StringBuilder();

			builder.append(
					"MSG_ID|PMT_INF_ID|INSTRUCTION_ID|END_TO_END_ID|DEBTOR_NAME|DEBTOR_ACCOUNT|CREDITOR_NAME|CREDITOR_ACCOUNT|AMOUNT|CURRENCY|BANK_CODE|REMITTANCE_INFO")
					.append(System.lineSeparator());

			for (Pain001Transaction tx : transactions) {
				builder.append(safe(pain001File.getMsgId())).append("|").append(safe(tx.getPaymentInfoId())).append("|")
						.append(safe(tx.getInstructionId())).append("|").append(safe(tx.getEndToEndId())).append("|")
						.append(safe(tx.getDebtorName())).append("|").append(safe(tx.getDebtorIban())).append("|")
						.append(safe(tx.getCreditorName())).append("|").append(safe(tx.getCreditorIban())).append("|")
						.append(formatAmount(tx.getAmount())).append("|").append(safe(tx.getCurrency())).append("|")
						.append(safe(bankCode)).append("|").append(safe(tx.getRemittanceInfo()))
						.append(System.lineSeparator());
			}

			Files.writeString(filePath, builder.toString());

			return filePath.toAbsolutePath().toString();

		} catch (Exception e) {
			throw new RuntimeException("Erreur génération fichier plat T24 : " + e.getMessage(), e);
		}
	}

	private String resolveBankCode(Pain001Transaction tx) {
		if (tx == null) {
			return null;
		}

		if (!isBlank(tx.getCreditorBankBic())) {
			return tx.getCreditorBankBic();
		}

		if (!isBlank(tx.getCreditorBankMemberId())) {
			return tx.getCreditorBankMemberId();
		}

		return extractBankCodeFromRib(tx.getCreditorIban());
	}

	private String extractBankCodeFromRib(String rib) {
		if (isBlank(rib) || rib.length() < 3) {
			return null;
		}

		return rib.substring(0, 3);
	}

	private String saveXmlFile(String fileName, String xml) {
		try {
			Path outputDir = Paths.get("output/pacs008");
			Files.createDirectories(outputDir);

			Path filePath = outputDir.resolve(fileName);
			Files.writeString(filePath, xml != null ? xml : "");

			return filePath.toAbsolutePath().toString();

		} catch (Exception e) {
			throw new RuntimeException("Erreur sauvegarde fichier pacs.008 : " + e.getMessage(), e);
		}
	}

	private String resolveCurrency(List<Pain001Transaction> transactions) {
		if (transactions == null || transactions.isEmpty()) {
			return null;
		}

		return transactions.stream().filter(tx -> tx != null).map(Pain001Transaction::getCurrency)
				.filter(currency -> !isBlank(currency)).findFirst().orElse("MAD");
	}

	private BigDecimal sumAmount(List<Pain001Transaction> transactions) {
		return transactions.stream().filter(tx -> tx != null).map(Pain001Transaction::getAmount)
				.filter(amount -> amount != null).reduce(BigDecimal.ZERO, BigDecimal::add)
				.setScale(2, RoundingMode.HALF_UP);
	}

	private String formatAmount(BigDecimal amount) {
		if (amount == null) {
			return "0.00";
		}

		return amount.setScale(2, RoundingMode.HALF_UP).toPlainString();
	}

	private String cleanFileName(String value) {
		if (isBlank(value)) {
			return "UNKNOWN";
		}

		return value.replaceAll("[^a-zA-Z0-9-_]", "_");
	}

	private String safe(String value) {
		return value == null ? "" : value.replace("|", " ");
	}

	private boolean isBlank(String value) {
		return value == null || value.trim().isEmpty();
	}
}