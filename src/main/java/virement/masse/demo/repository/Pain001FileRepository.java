package virement.masse.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import virement.masse.demo.model.Pain001File;

public interface Pain001FileRepository
        extends JpaRepository<Pain001File, Long>,
                JpaSpecificationExecutor<Pain001File> {

    Optional<Pain001File> findByMsgId(String msgId);

    boolean existsByMsgId(String msgId);
}