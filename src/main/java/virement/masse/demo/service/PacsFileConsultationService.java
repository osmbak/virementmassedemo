package virement.masse.demo.service;



import virement.masse.demo.dto.PacsFileDetailResponse;
import virement.masse.demo.dto.PacsFileResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PacsFileConsultationService {

    Page<PacsFileResponse> rechercherFichiers(
            String msgId,
            String nomFichier,
            String statut,
            String dateDebut,
            String dateFin,
            Pageable pageable
    );

    PacsFileDetailResponse consulterFichier(String id);
}