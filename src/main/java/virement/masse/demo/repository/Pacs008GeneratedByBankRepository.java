package virement.masse.demo.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import virement.masse.demo.model.Pacs008GeneratedByBank;

import java.util.List;
import java.util.Optional;

public interface Pacs008GeneratedByBankRepository
        extends JpaRepository<Pacs008GeneratedByBank, Long> {

    List<Pacs008GeneratedByBank> findByOriginalPainMsgId(String originalPainMsgId);

    List<Pacs008GeneratedByBank> findByCreditorBankBic(String creditorBankBic);

    Optional<Pacs008GeneratedByBank> findByGeneratedMsgId(String generatedMsgId);
}
