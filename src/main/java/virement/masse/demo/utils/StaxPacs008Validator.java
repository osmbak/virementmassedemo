package virement.masse.demo.utils;



import org.springframework.stereotype.Component;
import virement.masse.demo.exception.BusinessValidationException;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;

@Component
public class StaxPacs008Validator {

    public void validate(XMLStreamReader reader) {

        boolean hasMsgId = false;
        boolean hasNbOfTxs = false;
        boolean hasTx = false;

        try {
            while (reader.hasNext()) {

                int event = reader.next();

                if (event == XMLStreamConstants.START_ELEMENT) {

                    String tag = reader.getLocalName();

                    switch (tag) {

                        case "MsgId":
                            hasMsgId = true;
                            String msgId = reader.getElementText();
                            validateMsgIdLength(msgId);
                            break;

                        case "NbOfTxs":
                            hasNbOfTxs = true;
                            break;

                        case "CdtTrfTxInf":
                            hasTx = true;
                            break;
                    }
                }
            }

        } catch (Exception e) {
            throw new BusinessValidationException("Erreur validation StAX : " + e.getMessage());
        }

        if (!hasMsgId) {
            throw new BusinessValidationException("MsgId obligatoire manquant");
        }

        if (!hasNbOfTxs) {
            throw new BusinessValidationException("NbOfTxs obligatoire manquant");
        }

        if (!hasTx) {
            throw new BusinessValidationException("Aucune transaction trouvée");
        }
    }

    private void validateMsgIdLength(String msgId) {
        if (msgId == null || msgId.isEmpty()) {
            throw new BusinessValidationException("MsgId vide");
        }

        if (msgId.length() > 35) {
            throw new BusinessValidationException("MsgId dépasse 35 caractères");
        }
    }
}
