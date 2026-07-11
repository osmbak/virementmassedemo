package virement.masse.demo.impl;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import virement.masse.demo.dto.Pain001FileResponse;
import virement.masse.demo.model.Pain001File;
import virement.masse.demo.recherche.Pain001FileSpecification;
import virement.masse.demo.repository.Pain001FileRepository;
import virement.masse.demo.service.Pain001ConsultationService;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class Pain001ConsultationServiceImpl implements Pain001ConsultationService {

    private final Pain001FileRepository pain001FileRepository;

    public Pain001ConsultationServiceImpl(Pain001FileRepository pain001FileRepository) {
        this.pain001FileRepository = pain001FileRepository;
    }

    @Override
    public Page<Pain001FileResponse> rechercherPain001(
            String msgId,
            String fileName,
            String statut,
            String debtorName,
            String dateDebut,
            String dateFin,
            Pageable pageable
    ) {
        LocalDateTime debut = parseDateDebut(dateDebut);
        LocalDateTime fin = parseDateFin(dateFin);

        return pain001FileRepository
                .findAll(
                        Pain001FileSpecification.rechercher(
                                msgId,
                                fileName,
                                statut,
                                debtorName,
                                debut,
                                fin
                        ),
                        pageable
                )
                .map(this::toResponse);
    }

    private Pain001FileResponse toResponse(Pain001File file) {
        return new Pain001FileResponse(
                file.getId(),
                file.getMsgId(),
                file.getFileName(),
                file.getCreationDate(),
                file.getReceptionDate(),
                file.getNbOfTxs(),
                file.getCtrlSum(),
                file.getDebtorName(),
                file.getDebtorIban(),
                file.getStatut(),
                file.getErrorMessage()
        );
    }

    private LocalDateTime parseDateDebut(String date) {
        if (date == null || date.isBlank()) {
            return null;
        }
        return LocalDate.parse(date).atStartOfDay();
    }

    private LocalDateTime parseDateFin(String date) {
        if (date == null || date.isBlank()) {
            return null;
        }
        return LocalDate.parse(date).atTime(23, 59, 59);
    }
}