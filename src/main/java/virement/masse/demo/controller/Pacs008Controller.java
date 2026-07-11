package virement.masse.demo.controller;

import org.springframework.http.ResponseEntity;

import java.io.File;
import java.nio.file.Files;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import virement.masse.demo.dto.ParseResult;
import virement.masse.demo.generateur.Pacs002Generator;
import virement.masse.demo.model.Pacs002;
import virement.masse.demo.model.Pacs008Request;
import virement.masse.demo.parseur.Pacs008StaxParser;

@RestController
@RequestMapping("/virementmasse/api/pacs008")
public class Pacs008Controller {

	@Autowired
	private Pacs008StaxParser staxParser;
	@Autowired
	private Pacs002Generator pacs002Generator;

	@PostMapping(value = "/process", produces = "application/xml")
	public ResponseEntity<String> process(@RequestParam("file") MultipartFile file) {

		try {
			File temp = File.createTempFile("pacs008_", ".xml");
			file.transferTo(temp);

			Pacs002 result = staxParser.parseAndGeneratePacs002(temp);
			String xml = pacs002Generator.generatePacs002(result.getMsgId(), result.getMsgId(), // ou originalMsgId si
																								// différent
					LocalDateTime.now().toString(), result.getStatus(), result.getTxStatuses());

			return ResponseEntity.ok(xml);
		} catch (Exception e) {
			return ResponseEntity.status(500).body("<error>" + e.getMessage() + "</error>");
		}
	}

	@PostMapping("/upload-json")
	public ResponseEntity<String> uploadJson(@RequestBody Pacs008Request request) {

		File tempFile = null;

		try {
			if (request.getContentXml() == null || request.getContentXml().isEmpty()) {
				return ResponseEntity.badRequest().body("XML vide");
			}

			if (!"pacs008".equalsIgnoreCase(request.getXmlTag())) {
				return ResponseEntity.badRequest().body("xmlTag invalide");
			}

			// 1. créer fichier temporaire
			
			tempFile = File.createTempFile("pacs008_", ".xml");

			// 2. écrire XML string dans fichier
			
			Files.writeString(tempFile.toPath(), request.getContentXml());

			// 3. parser avec TON parser existant
			
			staxParser.parseAndGeneratePacs002(tempFile);

			return ResponseEntity.ok("JSON traité avec succès");

		} catch (Exception e) {
			return ResponseEntity.status(500).body("Erreur: " + e.getMessage());

		} finally {
			if (tempFile != null)
				tempFile.delete();
		}
	}
}
