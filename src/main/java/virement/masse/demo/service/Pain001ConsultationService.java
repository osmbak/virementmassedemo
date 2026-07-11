package virement.masse.demo.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import virement.masse.demo.dto.Pain001FileResponse;

public interface Pain001ConsultationService {

	Page<Pain001FileResponse> rechercherPain001(String msgId, String fileName, String statut, String debtorName,
			String dateDebut, String dateFin, Pageable pageable);
}