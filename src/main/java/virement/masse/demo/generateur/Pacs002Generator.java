package virement.masse.demo.generateur;

import org.springframework.stereotype.Component;
import virement.masse.demo.dto.TxStatus;
import virement.masse.demo.model.Pacs002;
import virement.masse.demo.model.PacsStatus;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.List;

@Component
public class Pacs002Generator {

 
	public String generatePacs002(String msgId, String originalMsgId, String creationDate, PacsStatus grpStatus,
			List<TxStatus> txStatuses) {

		try {

			XMLOutputFactory factory = XMLOutputFactory.newInstance();
			StringWriter sw = new StringWriter();

			XMLStreamWriter xml = factory.createXMLStreamWriter(sw);

// ================= ROOT =================
			
			xml.writeStartDocument("UTF-8", "1.0");

			xml.writeStartElement("Document");
			xml.writeDefaultNamespace("urn:iso:std:iso:20022:tech:xsd:pacs.002.001.10");

			xml.writeStartElement("FIToFIPmtStsRpt");

// ================= HEADER =================
			xml.writeStartElement("GrpHdr");
			writeTag(xml, "MsgId", msgId);
			writeTag(xml, "CreDtTm", creationDate);
			xml.writeEndElement();

// ================= ORIGINAL INFO =================
			xml.writeStartElement("OrgnlGrpInfAndSts");

			writeTag(xml, "OrgnlMsgId", originalMsgId);
			writeTag(xml, "OrgnlMsgNmId", "pacs.008.001.08");
			writeTag(xml, "GrpSts", grpStatus.name());

			xml.writeEndElement();

// ================= TRANSACTIONS =================
			for (TxStatus tx : txStatuses) {

				xml.writeStartElement("TxInfAndSts");

				writeTag(xml, "OrgnlInstrId", tx.getInstrId());
				writeTag(xml, "OrgnlEndToEndId", tx.getEndToEndId());
				writeTag(xml, "TxSts", tx.getStatus());

				if (tx.getReason() != null) {

					xml.writeStartElement("StsRsnInf");
					xml.writeStartElement("Rsn");

					writeTag(xml, "Cd", tx.getReason().getCode());

					xml.writeEndElement(); // Rsn
					xml.writeEndElement(); // StsRsnInf
				}

				xml.writeEndElement(); // TxInfAndSts
			}

// ================= CLOSE =================
			xml.writeEndElement(); // FIToFIPmtStsRpt
			xml.writeEndElement(); // Document

			xml.writeEndDocument();
			xml.flush();
			xml.close();

			return sw.toString();

		} catch (Exception e) {
			throw new RuntimeException("Erreur génération PACS.002 BAM", e);
		}
	}

	private void writeTag(XMLStreamWriter xml, String tag, String value) throws Exception {
		xml.writeStartElement(tag);
		if (value != null) {
			xml.writeCharacters(value);
		}
		xml.writeEndElement();
	}
}