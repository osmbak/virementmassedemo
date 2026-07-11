package virement.masse.demo.utils;


import org.springframework.stereotype.Component;
import virement.masse.demo.exception.BusinessValidationException;

import java.math.BigDecimal;

@Component
public class CftConsistencyValidator {

    public void validateMsgId(String xmlMsgId, String cftMsgId) {

        if (xmlMsgId == null || cftMsgId == null) {
            throw new BusinessValidationException("MsgId manquant");
        }

        if (!xmlMsgId.equals(cftMsgId)) {
            throw new BusinessValidationException(
                    "Incohérence MsgId XML vs CFT"
            );
        }
    }

    public void validateCtrlSum(BigDecimal xmlSum, BigDecimal cftSum) {

        if (xmlSum == null || cftSum == null) {
            throw new BusinessValidationException("CtrlSum manquant");
        }

        if (xmlSum.compareTo(cftSum) != 0) {
            throw new BusinessValidationException(
                    "Incohérence CtrlSum XML vs CFT"
            );
        }
    }
}