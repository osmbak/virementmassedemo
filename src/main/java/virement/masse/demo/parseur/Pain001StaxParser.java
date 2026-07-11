package virement.masse.demo.parseur;

import org.springframework.stereotype.Component;
import virement.masse.demo.model.Pain001File;
import virement.masse.demo.model.Pain001Transaction;

import javax.xml.stream.*;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayDeque;
import java.util.Deque;

@Component
public class Pain001StaxParser {

	public Pain001File parse(InputStream inputStream) {

		Pain001File painFile = new Pain001File();
		painFile.setCreationDate(LocalDateTime.now());

		Pain001Transaction currentTx = null;
		String currentPaymentInfoId = null;
		String lastTag = null;

		Deque<String> path = new ArrayDeque<>();
		XMLInputFactory factory = XMLInputFactory.newInstance();

		try {
			XMLStreamReader reader = factory.createXMLStreamReader(inputStream);

			while (reader.hasNext()) {
				int event = reader.next();

				if (event == XMLStreamConstants.START_ELEMENT) {

					String tag = reader.getLocalName();
					path.addLast(tag);
					lastTag = tag;

					if ("CdtTrfTxInf".equals(tag)) {
						currentTx = new Pain001Transaction();
						currentTx.setPaymentInfoId(currentPaymentInfoId);
						currentTx.setDebtorName(painFile.getDebtorName());
						currentTx.setDebtorIban(painFile.getDebtorIban());
					}

					if ("InstdAmt".equals(tag) && currentTx != null) {
						String ccy = reader.getAttributeValue(null, "Ccy");
						if (ccy != null && !ccy.isBlank()) {
							currentTx.setCurrency(ccy);
						}
					}
				}

				if (event == XMLStreamConstants.CHARACTERS) {

					String value = reader.getText() != null ? reader.getText().trim() : "";

					if (value.isEmpty() || lastTag == null) {
						continue;
					}

					String currentPath = "/" + String.join("/", path);

					if (currentPath.endsWith("/GrpHdr/MsgId")) {
						painFile.setMsgId(value);

					} else if (currentPath.endsWith("/GrpHdr/CreDtTm")) {
						painFile.setCreationDate(parseDate(value));

					} else if (currentPath.endsWith("/GrpHdr/NbOfTxs")) {
						painFile.setNbOfTxs(parseInteger(value));

					} else if (currentPath.endsWith("/GrpHdr/CtrlSum")) {
						painFile.setCtrlSum(parseAmount(value));

					} else if (currentPath.endsWith("/PmtInf/PmtInfId")) {
						currentPaymentInfoId = value;
						if (currentTx != null) {
							currentTx.setPaymentInfoId(value);
						}

					} else if (currentPath.endsWith("/PmtInf/Dbtr/Nm")) {
						painFile.setDebtorName(value);

					} else if (currentPath.endsWith("/PmtInf/DbtrAcct/Id/IBAN")
							|| currentPath.endsWith("/PmtInf/DbtrAcct/Id/Othr/Id")) {
						painFile.setDebtorIban(value);

					} else if (currentTx != null && currentPath.endsWith("/CdtTrfTxInf/PmtId/InstrId")) {
						currentTx.setInstructionId(value);

					} else if (currentTx != null && currentPath.endsWith("/CdtTrfTxInf/PmtId/EndToEndId")) {
						currentTx.setEndToEndId(value);

					} else if (currentTx != null && currentPath.endsWith("/CdtTrfTxInf/Amt/InstdAmt")) {
						currentTx.setAmount(parseAmount(value));

					} else if (currentTx != null && currentPath.endsWith("/CdtTrfTxInf/CdtrAgt/FinInstnId/BICFI")) {
						currentTx.setCreditorBankBic(value);

					} else if (currentTx != null
							&& currentPath.endsWith("/CdtTrfTxInf/CdtrAgt/FinInstnId/ClrSysMmbId/MmbId")) {
						currentTx.setCreditorBankMemberId(value);

					} else if (currentTx != null && currentPath.endsWith("/CdtTrfTxInf/Cdtr/Nm")) {
						currentTx.setCreditorName(value);

					} else if (currentTx != null && (currentPath.endsWith("/CdtTrfTxInf/CdtrAcct/Id/IBAN")
							|| currentPath.endsWith("/CdtTrfTxInf/CdtrAcct/Id/Othr/Id"))) {
						currentTx.setCreditorIban(value);

					} else if (currentTx != null && currentPath.endsWith("/CdtTrfTxInf/RmtInf/Ustrd")) {
						currentTx.setRemittanceInfo(value);
					}
				}

				if (event == XMLStreamConstants.END_ELEMENT) {

					String tag = reader.getLocalName();

					if ("CdtTrfTxInf".equals(tag) && currentTx != null) {

						if (currentTx.getDebtorName() == null) {
							currentTx.setDebtorName(painFile.getDebtorName());
						}

						if (currentTx.getDebtorIban() == null) {
							currentTx.setDebtorIban(painFile.getDebtorIban());
						}

						painFile.addTransaction(currentTx);
						currentTx = null;
					}

					if (!path.isEmpty()) {
						path.removeLast();
					}

					lastTag = path.peekLast();
				}
			}

			painFile.setStatut("PARSE_OK");

		} catch (Exception e) {
			painFile.setStatut("PARSE_KO");
			painFile.setErrorMessage(e.getMessage());
		}

		return painFile;
	}

	private Integer parseInteger(String value) {
		try {
			return value == null || value.isBlank() ? null : Integer.parseInt(value.trim());
		} catch (Exception e) {
			return null;
		}
	}
	private BigDecimal parseAmount(String value) {
	    try {
	        return value == null || value.isBlank()
	                ? null
	                : new BigDecimal(value.trim());
	    } catch (Exception e) {
	        return null;
	    }
	}

	/*
	 * private Double parseDouble(String value) { try { return value == null ||
	 * value.isBlank() ? null : Double.parseDouble(value.trim()); } catch (Exception
	 * e) { return null; } }
	 */

	private LocalDateTime parseDate(String value) {
		try {
			return value == null || value.isBlank() ? LocalDateTime.now() : LocalDateTime.parse(value.trim());
		} catch (Exception e) {
			return LocalDateTime.now();
		}
	}
}