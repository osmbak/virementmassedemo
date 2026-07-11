package virement.masse.demo.utils;

import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.StringReader;

@Component
public class Pacs008XsdValidator {

    private static final String PACS008_XSD_PATH = "xsd/pacs.008.001.13.xsd";

    public void validate(String xml) {

        try {
            SchemaFactory factory =
                    SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

            Schema schema = factory.newSchema(
                    new StreamSource(
                            getClass().getClassLoader().getResourceAsStream(PACS008_XSD_PATH)
                    )
            );

            Validator validator = schema.newValidator();

            validator.validate(
                    new StreamSource(new StringReader(xml))
            );

        } catch (SAXException e) {
            throw new IllegalArgumentException(
                    "XML pacs.008 non valide XSD : " + e.getMessage(), e
            );

        } catch (Exception e) {
            throw new RuntimeException(
                    "Erreur validation XSD pacs.008 : " + e.getMessage(), e
            );
        }
    }
}