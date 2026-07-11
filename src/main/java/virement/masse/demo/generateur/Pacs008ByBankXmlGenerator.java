package virement.masse.demo.generateur;

import org.springframework.stereotype.Component;
import virement.masse.demo.model.Pain001File;
import virement.masse.demo.model.Pain001Transaction;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.io.StringReader;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

@Component
public class Pacs008ByBankXmlGenerator {

	private static final String CODE_BANQUE_EMETTRICE = "190";
	private static final String BIC_EMETTEUR = "BCPOMAMCXXX";
	private static final String BIC_GSIMT = "GSIMTMA1XXX";

	public String generateXml(Pain001File painFile, String bankCode, List<Pain001Transaction> transactions,
			String generatedMsgId) {

		if (painFile == null) {
			throw new IllegalArgumentException("Pain001File est null");
		}
		if (bankCode == null || bankCode.isBlank()) {
			throw new IllegalArgumentException("Code banque bénéficiaire manquant");
		}
		if (transactions == null || transactions.isEmpty()) {
			throw new IllegalArgumentException("Aucune transaction à générer");
		}
		if (generatedMsgId == null || generatedMsgId.isBlank()) {
			throw new IllegalArgumentException("MsgId PACS008 manquant");
		}

		try {
			StringWriter stringWriter = new StringWriter();
			XMLStreamWriter writer = XMLOutputFactory.newInstance().createXMLStreamWriter(stringWriter);

			writer.writeStartDocument("UTF-8", "1.0");
			writer.writeStartElement("Document");
			writer.writeDefaultNamespace("urn:iso:std:iso:20022:tech:xsd:pacs.008.001.08");

			writer.writeStartElement("FIToFICstmrCdtTrf");

			writeGroupHeader(writer, generatedMsgId, transactions, bankCode);

			AtomicInteger txSequence = new AtomicInteger(1);
			for (Pain001Transaction tx : transactions) {
				writeTransaction(writer, tx, bankCode, generatedMsgId, txSequence.getAndIncrement());
			}

			writer.writeEndElement(); // FIToFICstmrCdtTrf
			writer.writeEndElement(); // Document
			writer.writeEndDocument();

			writer.flush();
			writer.close();

			// return stringWriter.toString();

			// XML brut généré
			String xml = stringWriter.toString();

			// Formatage du XML
			Transformer transformer = TransformerFactory.newInstance().newTransformer();

			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

			StringWriter formattedWriter = new StringWriter();

			transformer.transform(new StreamSource(new StringReader(xml)), new StreamResult(formattedWriter));

			return formattedWriter.toString();

		} catch (Exception e) {
			throw new RuntimeException("Erreur génération pacs.008 : " + e.getMessage(), e);
		}
	}

	private void writeGroupHeader(XMLStreamWriter writer, String generatedMsgId, List<Pain001Transaction> transactions,
			String bankCode) throws Exception {

		writer.writeStartElement("GrpHdr");

		writeElement(writer, "MsgId", generatedMsgId);
		writeElement(writer, "CreDtTm", LocalDateTime.now().toString());
		writeElement(writer, "NbOfTxs", String.valueOf(transactions.size()));
		writeElement(writer, "CtrlSum", formatAmount(sumAmount(transactions)));

		writer.writeStartElement("SttlmInf");

		writeElement(writer, "SttlmMtd", "CLRG");

		writer.writeStartElement("ClrSys");
		writeElement(writer, "Prtry", "GSIMT");
		writer.writeEndElement(); // ClrSys

		writer.writeEndElement(); // SttlmInf

		writeAgent(writer, "InstgAgt", BIC_EMETTEUR, CODE_BANQUE_EMETTRICE);
		writeAgent(writer, "InstdAgt", BIC_GSIMT, bankCode);

		writer.writeEndElement(); // GrpHdr
	}

	private void writeTransaction(XMLStreamWriter writer, Pain001Transaction tx, String bankCode, String generatedMsgId,
			int sequence) throws Exception {

		writer.writeStartElement("CdtTrfTxInf");

		writer.writeStartElement("PmtId");
		writeElement(writer, "InstrId", safe(tx.getInstructionId()));
		writeElement(writer, "EndToEndId", safe(tx.getEndToEndId()));
		writeElement(writer, "TxId", generateTxId(generatedMsgId, sequence));
		writer.writeEndElement(); // PmtId

		writer.writeStartElement("PmtTpInf");
		writeElement(writer, "InstrPrty", "NORM");
		writeElement(writer, "ClrChanl", "RTNS");

		writer.writeStartElement("SvcLvl");
		writeElement(writer, "Prtry", "PB2B");
		writer.writeEndElement();

		writer.writeStartElement("LclInstrm");
		writeElement(writer, "Prtry", "VIRM");
		writer.writeEndElement();

		writer.writeStartElement("CtgyPurp");
		writeElement(writer, "Prtry", "PAIEMENTS_FOURNISSEUR");
		writer.writeEndElement();

		writer.writeEndElement(); // PmtTpInf

		writer.writeStartElement("IntrBkSttlmAmt");
		writer.writeAttribute("Ccy", safe(tx.getCurrency(), "MAD"));
		writer.writeCharacters(formatAmount(tx.getAmount()));
		writer.writeEndElement();

		writeElement(writer, "IntrBkSttlmDt", LocalDate.now().toString());
		writeElement(writer, "ChrgBr", "SLEV");

		writeAgent(writer, "InstgAgt", BIC_EMETTEUR, CODE_BANQUE_EMETTRICE);
		writeAgent(writer, "InstdAgt", BIC_GSIMT, bankCode);

		writer.writeStartElement("Dbtr");
		writeElement(writer, "Nm", safe(tx.getDebtorName()));
		writeElement(writer, "CtryOfRes", "MA");
		writer.writeEndElement();

		writeAccountOther(writer, "DbtrAcct", safe(tx.getDebtorIban()), safe(tx.getCurrency(), "MAD"));

		writeAgent(writer, "DbtrAgt", BIC_EMETTEUR, CODE_BANQUE_EMETTRICE);

		writer.writeStartElement("CdtrAgt");
		writer.writeStartElement("FinInstnId");
		writer.writeStartElement("ClrSysMmbId");
		writeElement(writer, "MmbId", bankCode);
		writer.writeEndElement();
		writer.writeEndElement();
		writer.writeEndElement();

		writer.writeStartElement("Cdtr");
		writeElement(writer, "Nm", safe(tx.getCreditorName()));
		writer.writeEndElement();

		writeAccountOther(writer, "CdtrAcct", safe(tx.getCreditorIban()), safe(tx.getCurrency(), "MAD"));

		if (tx.getRemittanceInfo() != null && !tx.getRemittanceInfo().isBlank()) {
			writer.writeStartElement("RmtInf");
			writeElement(writer, "Ustrd", tx.getRemittanceInfo());
			writer.writeEndElement();
		}

		writer.writeEndElement(); // CdtTrfTxInf
	}

	private void writeAgent(XMLStreamWriter writer, String tagName, String bic, String memberId) throws Exception {
		writer.writeStartElement(tagName);
		writer.writeStartElement("FinInstnId");

		writeElement(writer, "BICFI", bic);

		writer.writeStartElement("ClrSysMmbId");

		writer.writeStartElement("ClrSysId");
		writeElement(writer, "Prtry", "GSIMT");
		writer.writeEndElement();

		writeElement(writer, "MmbId", memberId);

		writer.writeEndElement(); // ClrSysMmbId
		writer.writeEndElement(); // FinInstnId
		writer.writeEndElement(); // Agent
	}

	private void writeAccountOther(XMLStreamWriter writer, String tagName, String rib, String currency)
			throws Exception {
		writer.writeStartElement(tagName);

		writer.writeStartElement("Id");
		writer.writeStartElement("Othr");
		writeElement(writer, "Id", rib);
		writer.writeEndElement();
		writer.writeEndElement();

		writer.writeStartElement("Tp");
		writeElement(writer, "Prtry", "RIB");
		writer.writeEndElement();

		writeElement(writer, "Ccy", currency);

		writer.writeEndElement();
	}

	private String generateTxId(String generatedMsgId, int sequence) {
		return generatedMsgId.substring(0, 32) + String.format("%03d", sequence);
	}

	private BigDecimal sumAmount(List<Pain001Transaction> transactions) {
		return transactions.stream().map(Pain001Transaction::getAmount).filter(Objects::nonNull).reduce(BigDecimal.ZERO,
				BigDecimal::add);
	}

	private BigDecimal toBigDecimal(Double value) {
		return value == null ? BigDecimal.ZERO : BigDecimal.valueOf(value);
	}

	private String formatAmount(BigDecimal amount) {
		return amount.setScale(2, RoundingMode.HALF_UP).toPlainString();
	}

	private void writeElement(XMLStreamWriter writer, String name, String value) throws Exception {
		writer.writeStartElement(name);
		writer.writeCharacters(value != null ? value : "");
		writer.writeEndElement();
	}

	private String safe(String value) {
		return value == null ? "" : value;
	}

	private String safe(String value, String defaultValue) {
		return value == null || value.isBlank() ? defaultValue : value;
	}
}