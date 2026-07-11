package virement.masse.demo.recherche;

import org.springframework.data.jpa.domain.Specification;
import virement.masse.demo.model.Pacs008Transaction;

public class PacsTransactionSpecification {

    public static Specification<Pacs008Transaction> rechercher(
            String msgId,
            String instrId,
            String endToEndId,
            String compteDebiteur,
            String compteCrediteur,
            String statut
    ) {
        return (root, query, criteriaBuilder) -> {
            var predicate = criteriaBuilder.conjunction();

            if (msgId != null && !msgId.isBlank()) {
                predicate = criteriaBuilder.and(
                        predicate,
                        criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("pacsFile").get("msgId")),
                                "%" + msgId.toLowerCase() + "%"
                        )
                );
            }

            if (instrId != null && !instrId.isBlank()) {
                predicate = criteriaBuilder.and(
                        predicate,
                        criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("txId")),
                                "%" + instrId.toLowerCase() + "%"
                        )
                );
            }

            if (endToEndId != null && !endToEndId.isBlank()) {
                predicate = criteriaBuilder.and(
                        predicate,
                        criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("endToEndId")),
                                "%" + endToEndId.toLowerCase() + "%"
                        )
                );
            }

            if (compteDebiteur != null && !compteDebiteur.isBlank()) {
                predicate = criteriaBuilder.and(
                        predicate,
                        criteriaBuilder.like(
                                root.get("debtorIban"),
                                "%" + compteDebiteur + "%"
                        )
                );
            }

            if (compteCrediteur != null && !compteCrediteur.isBlank()) {
                predicate = criteriaBuilder.and(
                        predicate,
                        criteriaBuilder.like(
                                root.get("creditorIban"),
                                "%" + compteCrediteur + "%"
                        )
                );
            }

            if (statut != null && !statut.isBlank()) {
                predicate = criteriaBuilder.and(
                        predicate,
                        criteriaBuilder.equal(root.get("statut"), statut)
                );
            }

            return predicate;
        };
    }
}