package virement.masse.demo.utils;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.File;

import java.io.IOException;

import java.io.InputStream;

@Component
public class XsdValidator {

	public void validate(File xmlFile, String xsdPath) {

		try (InputStream xsdStream = new ClassPathResource(xsdPath).getInputStream()) {

			SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

			Schema schema = schemaFactory.newSchema(new StreamSource(xsdStream));

			schema.newValidator().validate(new StreamSource(xmlFile));

		} catch (SAXException | IOException e) {
			throw new RuntimeException("Fichier XML invalide par rapport au schéma XSD : " + e.getMessage(), e);
		}
	}
}