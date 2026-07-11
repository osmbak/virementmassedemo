package virement.masse.demo.parseur;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import virement.masse.demo.dto.TxStatus;
import virement.masse.demo.exception.DuplicateFileException;
import virement.masse.demo.model.Pacs002;
import virement.masse.demo.model.Pacs008Transaction;
import virement.masse.demo.model.PacsFile;
import virement.masse.demo.model.PacsStatus;
import virement.masse.demo.repository.PacsFileRepository;
import virement.masse.demo.repository.PacsTransactionRepository;
import virement.masse.demo.utils.XsdValidator;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class Pacs008StaxParser {

	@Autowired
	private PacsFileRepository fileRepository;

	@Autowired
	private PacsTransactionRepository txRepository;

	@Autowired
	private Pacs008DataExtractor extractor;

	@Autowired
	private XsdValidator xsdValidator;

	@Transactional
	public Pacs002 parseAndGeneratePacs002(File file) throws DuplicateFileException {

		log.info("Début traitement fichier {}", file.getName());

		List<TxStatus> txStatuses = new ArrayList<>();
		PacsFile pacsFile = new PacsFile(file.getName());

		XMLInputFactory factory = XMLInputFactory.newInstance();

		try {

			/*
			 * 1. Validation XSD avant toute insertion en base
			 */
	//		xsdValidator.validate(file, "xsd/pacs.008.001.13.xsd");

			/*
			 * 2. Parsing StAX
			 */
			try (InputStream is = new FileInputStream(file)) {

				XMLStreamReader reader = factory.createXMLStreamReader(is);

				while (reader.hasNext()) {

					int event = reader.next();

					if (event == XMLStreamConstants.START_ELEMENT) {

						String tag = reader.getLocalName();

						/*
						 * HEADER PACS008
						 */
						if ("GrpHdr".equals(tag)) {

							extractor.fillHeaderData(reader, pacsFile);

							pacsFile = savePacsFileSafely(pacsFile);
						}

						/*
						 * TRANSACTION PACS008
						 */
						if ("CdtTrfTxInf".equals(tag)) {

							Pacs008Transaction tx = new Pacs008Transaction();
							tx.setTxId(null);
							tx.setPacsFile(pacsFile);

							extractor.fillTransactionData(reader, tx);

							TxStatus status = new TxStatus();
							status.setInstrId(tx.getTxId());
							status.setEndToEndId(tx.getEndToEndId());

							if (isValid(tx)) {

								txRepository.save(tx);
								status.setStatus("ACCP");

							} else {

								status.setStatus("RJCT");
							}

							txStatuses.add(status);
						}
					}
				}
			}

		} catch (DuplicateFileException e) {
			throw e;

		} catch (Exception e) {
			log.error("Erreur lors du traitement du fichier {}", file.getName(), e);
			throw new RuntimeException("Erreur lors du traitement du fichier PACS008 : " + file.getName(), e);
		}

		return buildPacs002(pacsFile, txStatuses);
	}

	private PacsFile savePacsFileSafely(PacsFile pacsFile) throws DuplicateFileException {

		fileRepository.findByMsgIdForUpdate(pacsFile.getMsgId()).ifPresent(existing -> {
			throw new RuntimeException("Fichier déjà traité : " + pacsFile.getMsgId());
		});

		try {

			pacsFile.setMsgId(null);
			return fileRepository.saveAndFlush(pacsFile);

		} catch (DataIntegrityViolationException e) {

			throw new DuplicateFileException("Fichier déjà traité en concurrence : " + pacsFile.getMsgId());

		} catch (RuntimeException e) {

			if (e.getMessage() != null && e.getMessage().contains("Fichier déjà traité")) {

				throw new DuplicateFileException(e.getMessage());
			}

			throw e;
		}
	}

	private boolean isValid(Pacs008Transaction tx) {

		if (tx.getAmount() == null || tx.getAmount() <= 0) {
			return false;
		}

		if (tx.getDebtorIban() == null || tx.getDebtorIban().isBlank()) {
			return false;
		}

		if (tx.getCreditorIban() == null || tx.getCreditorIban().isBlank()) {
			return false;
		}

		return true;
	}

	private Pacs002 buildPacs002(PacsFile file, List<TxStatus> txStatuses) {

		Pacs002 pacs002 = new Pacs002();

		pacs002.setMsgId(file.getMsgId());
		pacs002.setNbOfTxs(txStatuses.size());

		boolean hasRejectedTransaction = txStatuses.stream().anyMatch(tx -> "RJCT".equals(tx.getStatus()));

		pacs002.setStatus(hasRejectedTransaction ? PacsStatus.PART : PacsStatus.ACCP);

		pacs002.setTxStatuses(txStatuses);

		return pacs002;
	}
}