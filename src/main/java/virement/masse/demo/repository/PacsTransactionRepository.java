package virement.masse.demo.repository;


import virement.masse.demo.model.Pacs008Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PacsTransactionRepository
        extends JpaRepository<Pacs008Transaction, Long>,
                JpaSpecificationExecutor<Pacs008Transaction> {
}