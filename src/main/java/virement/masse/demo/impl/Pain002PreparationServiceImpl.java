package virement.masse.demo.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import virement.masse.demo.dto.pain002.Pacs002StatusResult;
import virement.masse.demo.generateur.Pain002XmlGenerator;
import virement.masse.demo.model.Pacs008GeneratedByBank;
import virement.masse.demo.model.Pain001File;
import virement.masse.demo.model.Pain001Transaction;
import virement.masse.demo.parseur.Pacs002StaxParser;
import virement.masse.demo.repository.Pacs008GeneratedByBankRepository;
import virement.masse.demo.repository.Pain001FileRepository;
import virement.masse.demo.service.Pain002PreparationService;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class Pain002PreparationServiceImpl implements Pain002PreparationService {

    private final Pacs002StaxParser pacs002StaxParser;
    private final Pacs008GeneratedByBankRepository pacs008Repository;
    private final Pain001FileRepository pain001FileRepository;
    private final Pain002XmlGenerator pain002XmlGenerator;

    public Pain002PreparationServiceImpl(
            Pacs002StaxParser pacs002StaxParser,
            Pacs008GeneratedByBankRepository pacs008Repository,
            Pain001FileRepository pain001FileRepository,
            Pain002XmlGenerator pain002XmlGenerator
    ) {
        this.pacs002StaxParser = pacs002StaxParser;
        this.pacs008Repository = pacs008Repository;
        this.pain001FileRepository = pain001FileRepository;
        this.pain002XmlGenerator = pain002XmlGenerator;
    }

    @Override
    public String preparePain002FromPacs002(MultipartFile multipartFile) {

        File tempFile = null;

        try {
            tempFile = File.createTempFile("pacs002_", ".xml");
            multipartFile.transferTo(tempFile);

            Pacs002StatusResult pacs002Result = pacs002StaxParser.parse(tempFile);

            Pacs008GeneratedByBank pacs008 =
                    pacs008Repository
                            .findByGeneratedMsgId(pacs002Result.getOriginalPacs008MsgId())
                            .orElseThrow(() -> new RuntimeException(
                                    "Aucun PACS008 trouvé avec MsgId : "
                                            + pacs002Result.getOriginalPacs008MsgId()
                            ));

            Pain001File pain001File = pacs008.getPain001File();

            for (Pain001Transaction tx : pain001File.getTransactions()) {

                String status = pacs002Result
                        .getTransactionStatuses()
                        .get(tx.getEndToEndId());

                if (status != null) {
                    tx.setStatut(status);
                }
            }

            pain001FileRepository.save(pain001File);

            String pain002Xml = pain002XmlGenerator.generate(pain001File);

            String fileName = "pain002_"
                    + cleanFileName(pain001File.getMsgId())
                    + "_"
                    + cleanFileName(pacs008.getCreditorBankBic())
                    + ".xml";

            return savePain002(fileName, pain002Xml);

        } catch (Exception e) {
            throw new RuntimeException("Erreur préparation Pain.002 : " + e.getMessage(), e);

        } finally {
            if (tempFile != null && tempFile.exists()) {
                tempFile.delete();
            }
        }
    }

    private String savePain002(String fileName, String xml) {
        try {
            Path outputDir = Paths.get("output/pain002");
            Files.createDirectories(outputDir);

            Path filePath = outputDir.resolve(fileName);
            Files.writeString(filePath, xml);

            return filePath.toAbsolutePath().toString();

        } catch (Exception e) {
            throw new RuntimeException("Erreur sauvegarde Pain.002 : " + e.getMessage(), e);
        }
    }

    private String cleanFileName(String value) {
        if (value == null || value.trim().isEmpty()) {
            return "UNKNOWN";
        }
        return value.replaceAll("[^a-zA-Z0-9-_]", "_");
    }
}