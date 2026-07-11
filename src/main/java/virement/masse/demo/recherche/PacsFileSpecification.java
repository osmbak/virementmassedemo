package virement.masse.demo.recherche;


import virement.masse.demo.model.PacsFile;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class PacsFileSpecification {

    public static Specification<PacsFile> rechercher(
            String msgId,
            String nomFichier,
            String statut,
            LocalDateTime dateDebut,
            LocalDateTime dateFin
    ) {
        return (root, query, criteriaBuilder) -> {
            var predicate = criteriaBuilder.conjunction();

            if (msgId != null && !msgId.isBlank()) {
                predicate = criteriaBuilder.and(
                        predicate,
                        criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("msgId")),
                                "%" + msgId.toLowerCase() + "%"
                        )
                );
            }

            if (nomFichier != null && !nomFichier.isBlank()) {
                predicate = criteriaBuilder.and(
                        predicate,
                        criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("nomFichier")),
                                "%" + nomFichier.toLowerCase() + "%"
                        )
                );
            }

            if (statut != null && !statut.isBlank()) {
                predicate = criteriaBuilder.and(
                        predicate,
                        criteriaBuilder.equal(root.get("statut"), statut)
                );
            }

            if (dateDebut != null) {
                predicate = criteriaBuilder.and(
                        predicate,
                        criteriaBuilder.greaterThanOrEqualTo(root.get("dateReception"), dateDebut)
                );
            }

            if (dateFin != null) {
                predicate = criteriaBuilder.and(
                        predicate,
                        criteriaBuilder.lessThanOrEqualTo(root.get("dateReception"), dateFin)
                );
            }

            return predicate;
        };
    }
}