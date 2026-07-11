package virement.masse.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import virement.masse.demo.model.PacsFile;

import jakarta.persistence.LockModeType;
import java.util.Optional;

public interface PacsFileRepository extends JpaRepository<PacsFile, Long> {

	boolean existsByMsgId(String msgId);

	Optional<PacsFile> findByMsgId(String msgId);

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("select f from PacsFile f where f.msgId = :msgId")
	Optional<PacsFile> findByMsgIdForUpdate(@Param("msgId") String msgId);
}