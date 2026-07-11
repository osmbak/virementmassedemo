package virement.masse.demo.utils;


import org.springframework.stereotype.Component;
import virement.masse.demo.exception.BusinessValidationException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

@Component
public class Pacs008FieldValidator {

    public void validate(Document doc) {

        NodeList msgId = doc.getElementsByTagName("MsgId");
        if (msgId.getLength() == 0) {
            throw new BusinessValidationException("MsgId obligatoire manquant");
        }

        NodeList nbTx = doc.getElementsByTagName("NbOfTxs");
        if (nbTx.getLength() == 0) {
            throw new BusinessValidationException("NbOfTxs obligatoire manquant");
        }

        NodeList tx = doc.getElementsByTagName("CdtTrfTxInf");
        if (tx.getLength() == 0) {
            throw new BusinessValidationException("Aucune transaction trouvée");
        }
    }
}