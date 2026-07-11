package virement.masse.demo.utils;


import org.springframework.stereotype.Component;
import virement.masse.demo.exception.BusinessValidationException;

@Component
public class Pacs008LengthValidator {

    public void validateMsgId(String msgId) {

        if (msgId == null || msgId.isEmpty()) {
            throw new BusinessValidationException("MsgId vide");
        }

        if (msgId.length() > 35) {
            throw new BusinessValidationException("MsgId dépasse 35 caractères");
        }
    }
}