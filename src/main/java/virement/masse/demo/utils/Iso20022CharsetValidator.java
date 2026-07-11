package virement.masse.demo.utils;


import org.springframework.stereotype.Component;
import virement.masse.demo.exception.BusinessValidationException;

import java.nio.charset.StandardCharsets;

@Component
public class Iso20022CharsetValidator {

    public void validate(String xml) {

        if (xml == null || xml.isEmpty()) {
            throw new BusinessValidationException("XML vide");
        }

        // ISO20022 = UTF-8
        if (!StandardCharsets.UTF_8.newEncoder().canEncode(xml)) {
            throw new BusinessValidationException(
                    "Caractères non conformes ISO20022 (UTF-8 requis)"
            );
        }
    }
}