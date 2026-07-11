package virement.masse.demo.generateur;

import org.springframework.stereotype.Component;
import virement.masse.demo.model.Pain001File;
import virement.masse.demo.model.Pain001Transaction;

import javax.xml.stream.*;
import java.io.StringWriter;
import java.time.LocalDateTime;

@Component
public class Pain002XmlGenerator {

    public String generate(Pain001File pain001File) {

        try {
            StringWriter sw = new StringWriter();
            XMLStreamWriter writer = XMLOutputFactory.newInstance()
                    .createXMLStreamWriter(sw);

            writer.writeStartDocument("UTF-8", "1.0");

            writer.writeStartElement("Document");
            writer.writeDefaultNamespace("urn:iso:std:iso:20022:tech:xsd:pain.002.001.10");

            writer.writeStartElement("CstmrPmtStsRpt");

            writer.writeStartElement("GrpHdr");
            writeElement(writer, "MsgId", "PAIN002-" + pain001File.getMsgId());
            writeElement(writer, "CreDtTm", LocalDateTime.now().toString());
            writer.writeEndElement();

            writer.writeStartElement("OrgnlGrpInfAndSts");
            writeElement(writer, "OrgnlMsgId", pain001File.getMsgId());
            writeElement(writer, "OrgnlMsgNmId", "pain.001.001.09");
            writeElement(writer, "GrpSts", resolveGroupStatus(pain001File));
            writer.writeEndElement();

            writer.writeStartElement("OrgnlPmtInfAndSts");
            writeElement(writer, "OrgnlPmtInfId", pain001File.getTransactions().get(0).getPaymentInfoId());
            writeElement(writer, "PmtInfSts", resolveGroupStatus(pain001File));

            for (Pain001Transaction tx : pain001File.getTransactions()) {
                writer.writeStartElement("TxInfAndSts");

                writeElement(writer, "OrgnlInstrId", tx.getInstructionId());
                writeElement(writer, "OrgnlEndToEndId", tx.getEndToEndId());
                writeElement(writer, "TxSts", normalizeStatus(tx.getStatut()));

                writer.writeEndElement();
            }

            writer.writeEndElement();

            writer.writeEndElement();
            writer.writeEndElement();

            writer.writeEndDocument();
            writer.close();

            return sw.toString();

        } catch (Exception e) {
            throw new RuntimeException("Erreur génération Pain.002 : " + e.getMessage(), e);
        }
    }

    private String resolveGroupStatus(Pain001File pain001File) {

        boolean hasAccepted = false;
        boolean hasRejected = false;

        for (Pain001Transaction tx : pain001File.getTransactions()) {
            String status = normalizeStatus(tx.getStatut());

            if ("ACCP".equals(status)) {
                hasAccepted = true;
            }

            if ("RJCT".equals(status)) {
                hasRejected = true;
            }
        }

        if (hasAccepted && hasRejected) {
            return "PART";
        }

        if (hasRejected) {
            return "RJCT";
        }

        return "ACCP";
    }

    private String normalizeStatus(String statut) {
        if (statut == null) {
            return "ACCP";
        }

        if ("RJCT".equalsIgnoreCase(statut) || "REJECTED".equalsIgnoreCase(statut)) {
            return "RJCT";
        }

        return "ACCP";
    }

    private void writeElement(XMLStreamWriter writer, String name, String value) throws Exception {
        writer.writeStartElement(name);
        writer.writeCharacters(value == null ? "" : value);
        writer.writeEndElement();
    }
}