/*
 * package virement.masse.demo.service;
 * 
 * 
 * import org.springframework.beans.factory.annotation.Autowired; import
 * org.springframework.stereotype.Service; import
 * org.springframework.web.multipart.MultipartFile;
 * 
 * import virement.masse.demo.dto.ParseResult; import
 * virement.masse.demo.dto.TxStatus; import
 * virement.masse.demo.generateur.Pacs002Generator; import
 * virement.masse.demo.model.PacsStatus; import
 * virement.masse.demo.parseur.Pacs008StaxParser;
 * 
 * import java.io.File; import java.util.List;
 * 
 * @Service public class Pacs008Service {
 * 
 * @Autowired private Pacs008StaxParser parser;
 * 
 * @Autowired private Pacs002Generator pacs002Generator;
 * 
 * public String processAndReturnPacs002(MultipartFile file) throws Exception {
 * 
 * // 1. convert MultipartFile → File temporaire File temp =
 * File.createTempFile("pacs008_", ".xml"); file.transferTo(temp);
 * 
 * // 2. parse + business result ParseResult result =
 * parser.parseAndBuildResult(temp);
 * 
 * // 3. build PACS.002 String outputPath = temp.getParent() + "/pacs002.xml";
 * 
 * pacs002Generator.generate( result.getMsgId(), result.getStatus(),
 * result.getTxStatuses(), outputPath );
 * 
 * // 4. return XML content return
 * java.nio.file.Files.readString(java.nio.file.Path.of(outputPath)); } }
 */