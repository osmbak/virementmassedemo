package virement.masse.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.file.dsl.Files;
import org.springframework.integration.dsl.Pollers;
import java.io.File;

import virement.masse.demo.exception.DuplicateFileException;
import virement.masse.demo.parseur.Pacs008StaxParser;

import org.springframework.beans.factory.annotation.Autowired;

@Configuration
public class CftFileFlowConfig {

	private final String PATH_IN = "C:/CFT/inbox";
	private final String PATH_ARCHIVE = "C:/CFT/archive";
	private final String PATH_DUPLICATE = "C:/CFT/duplicates";
	private final String PATH_ERROR = "C:/CFT/error";

	@Autowired
	private Pacs008StaxParser staxParser;

	@Bean
	public IntegrationFlow cftInboundFlow() {
		return IntegrationFlow
				// 1. On écoute le dossier (Scan toutes les 5 secondes)
				.from(Files.inboundAdapter(new File(PATH_IN)).patternFilter("*.xml"),
						e -> e.poller(Pollers.fixedDelay(5000)))

				// 2. On traite le fichier dans un bloc Handle
				.handle(File.class, (file, headers) -> {
				    try {
				        staxParser.parseAndGeneratePacs002(file);
				        moveFile(file, PATH_ARCHIVE);
				        
				    } catch (Exception e) {
				        // ON VÉRIFIE SI LA CAUSE EST UN DOUBLON
				        if (e.getCause() instanceof DuplicateFileException || e instanceof DuplicateFileException) {
				            System.out.println(">>> DOUBLON DÉTECTÉ : " + e.getMessage());
				            moveFile(file, PATH_DUPLICATE);
				        } else {
				            // C'est une vraie erreur technique (Base de données, XML cassé...)
				            System.err.println(">>> ERREUR CRITIQUE sur " + file.getName() + " : " + e.getMessage());
				            moveFile(file, PATH_ERROR);
				        }
				    }
				    return null;
				}).get();
	}

	private void moveFile(File file, String targetPath) {
		File targetDir = new File(targetPath);
		if (!targetDir.exists())
			targetDir.mkdirs();

		File dest = new File(targetDir, file.getName());
		// Suppression si un fichier de même nom existe déjà dans la destination
		if (dest.exists())
			dest.delete();

		if (!file.renameTo(dest)) {
			System.err.println("ALERTE : Impossible de déplacer le fichier vers " + targetPath);
		}
	}
}