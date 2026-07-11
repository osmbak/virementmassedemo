package virement.masse.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import virement.masse.demo.service.Pain002PreparationService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/virementmasse/api/pacs002")
public class Pacs002Controller {

	private final Pain002PreparationService pain002PreparationService;

	public Pacs002Controller(Pain002PreparationService pain002PreparationService) {
		this.pain002PreparationService = pain002PreparationService;
	}

	@PostMapping("/process")
	public ResponseEntity<Map<String, Object>> processPacs002(@RequestParam("file") MultipartFile file) {
		
		String pain002Path = pain002PreparationService.preparePain002FromPacs002(file);

		Map<String, Object> response = new HashMap<>();
		response.put("status", "SUCCESS");
		response.put("message", "PACS.002 traité avec succès, Pain.002 généré");
		response.put("pain002Path", pain002Path);

		return ResponseEntity.ok(response);
	}
}