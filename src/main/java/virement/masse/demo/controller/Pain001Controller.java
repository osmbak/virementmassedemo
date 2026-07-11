package virement.masse.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import virement.masse.demo.service.Pain001ConsultationService;
import virement.masse.demo.service.Pain001ProcessingService;

import java.io.IOException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import virement.masse.demo.dto.Pain001FileResponse;
import virement.masse.demo.dto.Pain001ProcessResponse;

@RestController
@RequestMapping("*")
public class Pain001Controller {

	private final Pain001ProcessingService pain001ProcessingService;

	private final Pain001ConsultationService pain001ConsultationService;

	public Pain001Controller(Pain001ProcessingService pain001ProcessingService,
			Pain001ConsultationService pain001ConsultationService) {
		this.pain001ProcessingService = pain001ProcessingService;
		this.pain001ConsultationService = pain001ConsultationService;
	}

	/*
	 * @PostMapping("/process") public ResponseEntity<?>
	 * processPain001(@RequestParam("file") MultipartFile file) {
	 * 
	 * try { return
	 * ResponseEntity.ok(pain001ProcessingService.processPain001(file));
	 * 
	 * } catch (IllegalArgumentException e) { return
	 * ResponseEntity.badRequest().body(e.getMessage());
	 * 
	 * } catch (Exception e) { return
	 * ResponseEntity.status(500).body(e.getMessage()); } }
	 */
	@PostMapping("/consomationpain001")
	public ResponseEntity<Pain001ProcessResponse> uploadPain001(@RequestParam("file") MultipartFile file)
			throws IOException {

		Pain001ProcessResponse response = pain001ProcessingService.processPain001(file.getInputStream(),
				file.getOriginalFilename());

		return ResponseEntity.ok(response);
	}

	@GetMapping("/search")
	public Page<Pain001FileResponse> rechercherPain001(@RequestParam(name = "msgId", required = false) String msgId,
			@RequestParam(name = "fileName", required = false) String fileName,
			@RequestParam(name = "statut", required = false) String statut,
			@RequestParam(name = "debtorName", required = false) String debtorName,
			@RequestParam(name = "dateDebut", required = false) String dateDebut,
			@RequestParam(name = "dateFin", required = false) String dateFin, Pageable pageable) {
		return pain001ConsultationService.rechercherPain001(msgId, fileName, statut, debtorName, dateDebut, dateFin,
				pageable);
	}
}
