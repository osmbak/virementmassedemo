package virement.masse.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import virement.masse.demo.model.Banque;
import virement.masse.demo.model.Pain001File;

import java.util.Optional;

@Repository
public interface BanqueRepository
        extends JpaRepository<Banque, String>{

}