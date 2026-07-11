package virement.masse.demo.parseur;

import org.springframework.stereotype.Component;
import virement.masse.demo.dto.pain002.Pacs002StatusResult;

import javax.xml.stream.*;
import java.io.File;
import java.io.FileInputStream;

@Component
public class Pacs002StaxParser {

	public Pacs002StatusResult parse(File file) {

		Pacs002StatusResult result = new Pacs002StatusResult();

		String currentTag = null;
		String currentEndToEndId = null;

		try (FileInputStream inputStream = new FileInputStream(file)) {

			XMLStreamReader reader = XMLInputFactory.newInstance().createXMLStreamReader(inputStream);

			while (reader.hasNext()) {
				int event = reader.next();

				if (event == XMLStreamConstants.START_ELEMENT) {
					currentTag = reader.getLocalName();
				}

				if (event == XMLStreamConstants.CHARACTERS) {
					String value = reader.getText().trim();

					if (value.isEmpty()) {
						continue;
					}

					if ("OrgnlMsgId".equals(currentTag)) {
						result.setOriginalPacs008MsgId(value);
					}

					if ("GrpSts".equals(currentTag)) {
						result.setGroupStatus(value);
					}

					if ("OrgnlEndToEndId".equals(currentTag)) {
						currentEndToEndId = value;
					}

					if ("TxSts".equals(currentTag) && currentEndToEndId != null) {
						result.getTransactionStatuses().put(currentEndToEndId, value);
						currentEndToEndId = null;
					}
				}
			}

			reader.close();
			return result;

		} catch (Exception e) {
			throw new RuntimeException("Erreur parsing PACS.002 : " + e.getMessage(), e);
		}
	}
}