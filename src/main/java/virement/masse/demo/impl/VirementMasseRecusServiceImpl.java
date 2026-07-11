package virement.masse.demo.impl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import virement.masse.demo.dto.consultationrecherche.DetailsFichierRecusRequest;
import virement.masse.demo.dto.consultationrecherche.DetailsFichierRecusResponse;
import virement.masse.demo.dto.consultationrecherche.ListeTransactionsRecusRequest;
import virement.masse.demo.dto.consultationrecherche.ListeTransactionsRecusResponse;
import virement.masse.demo.dto.consultationrecherche.RechercheFichiersRecusRequest;
import virement.masse.demo.dto.consultationrecherche.RechercheFichiersRecusResponse;
import virement.masse.demo.dto.consultationrecherche.DetailTransactionRecusRequest;
import virement.masse.demo.dto.consultationrecherche.DetailTransactionRecusResponse;

import virement.masse.demo.service.VirementMasseRecusService;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class VirementMasseRecusServiceImpl implements VirementMasseRecusService {

    @Override
    public RechercheFichiersRecusResponse rechercheFichiersRecus(
            RechercheFichiersRecusRequest request
    ) {

        /*
         * Ici tu dois appeler ton repository pour chercher les fichiers reçus.
         * Exemple :
         *
         * Page<PacsFile> page = pacsFileRepository.rechercherFichiersRecus(...);
         */

        return RechercheFichiersRecusResponse.builder()
                .content(Collections.emptyList())
                .page(0)
                .size(10)
                .totalElements(0)
                .totalPages(0)
                .build();
    }

    @Override
    public DetailsFichierRecusResponse detailsFichierRecus(
            DetailsFichierRecusRequest request
    ) {

        /*
         * Ici tu dois chercher le fichier par msgId.
         * Exemple :
         *
         * PacsFile file = pacsFileRepository.findByMsgId(request.getMsgId())
         *        .orElseThrow(() -> new RuntimeException("Fichier introuvable"));
         */

        return DetailsFichierRecusResponse.builder()
                .msgId(request.getMsgId())
                .build();
    }

    @Override
    public ListeTransactionsRecusResponse listeTransactionsRecus(
            ListeTransactionsRecusRequest request
    ) {

        /*
         * Ici tu dois chercher les transactions par msgId.
         * Exemple :
         *
         * Page<PacsTransaction> page =
         *      pacsTransactionRepository.findByMsgId(request.getMsgId(), pageable);
         */

        return ListeTransactionsRecusResponse.builder()
                .content(Collections.emptyList())
                .page(request.getNumeroPage() == null ? 0 : request.getNumeroPage())
                .size(10)
                .totalElements(0)
                .totalPages(0)
                .build();
    }

    @Override
    public DetailTransactionRecusResponse detailTransactionRecus(
            DetailTransactionRecusRequest request) {

        /*
         * Ici tu dois chercher une transaction par référence RIO.
         * Exemple :
         *
         * PacsTransaction tx =
         *      pacsTransactionRepository.findByReferenceRio(request.getReferenceRio())
         *      .orElseThrow(() -> new RuntimeException("Transaction introuvable"));
         */

        return DetailTransactionRecusResponse.builder()
                .referenceRio(request.getReferenceRio())
                .build();
    }
}