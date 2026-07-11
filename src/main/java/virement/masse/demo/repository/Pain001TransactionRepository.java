package virement.masse.demo.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import virement.masse.demo.model.Pain001Transaction;

import java.util.List;

public interface Pain001TransactionRepository extends JpaRepository<Pain001Transaction, Long> {

    List<Pain001Transaction> findByPain001FileId(Long pain001FileId);

    List<Pain001Transaction> findByCreditorBankBic(String creditorBankBic);
}
