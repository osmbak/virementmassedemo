package virement.masse.demo.service;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import virement.masse.demo.dto.PacsTransactionDTO;

public interface PacsTransactionConsultationService {

    Page<PacsTransactionDTO> rechercherTransactions(
            String msgId,
            String instrId,
            String endToEndId,
            String compteDebiteur,
            String compteCrediteur,
            String statut,
            Pageable pageable
    );
}