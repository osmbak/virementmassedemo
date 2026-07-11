package virement.masse.demo.recherche;


import org.springframework.data.jpa.domain.Specification;
import virement.masse.demo.model.Pain001File;

import java.time.LocalDateTime;

public class Pain001FileSpecification {

    public static Specification<Pain001File> rechercher(
            String msgId,
            String fileName,
            String statut,
            String debtorName,
            LocalDateTime dateDebut,
            LocalDateTime dateFin
    ) {
        return (root, query, cb) -> {
            var predicate = cb.conjunction();

            if (msgId != null && !msgId.isBlank()) {
                predicate = cb.and(predicate,
                        cb.like(cb.lower(root.get("msgId")),
                                "%" + msgId.toLowerCase() + "%"));
            }

            if (fileName != null && !fileName.isBlank()) {
                predicate = cb.and(predicate,
                        cb.like(cb.lower(root.get("fileName")),
                                "%" + fileName.toLowerCase() + "%"));
            }

            if (statut != null && !statut.isBlank()) {
                predicate = cb.and(predicate,
                        cb.equal(root.get("statut"), statut));
            }

            if (debtorName != null && !debtorName.isBlank()) {
                predicate = cb.and(predicate,
                        cb.like(cb.lower(root.get("debtorName")),
                                "%" + debtorName.toLowerCase() + "%"));
            }

            if (dateDebut != null) {
                predicate = cb.and(predicate,
                        cb.greaterThanOrEqualTo(root.get("receptionDate"), dateDebut));
            }

            if (dateFin != null) {
                predicate = cb.and(predicate,
                        cb.lessThanOrEqualTo(root.get("receptionDate"), dateFin));
            }

            return predicate;
        };
    }
}