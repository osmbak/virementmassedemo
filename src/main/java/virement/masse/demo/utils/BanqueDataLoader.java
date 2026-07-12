package virement.masse.demo.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import virement.masse.demo.model.Banque;
import virement.masse.demo.repository.BanqueRepository;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class BanqueDataLoader {

    private final BanqueRepository banqueRepository;

    @Bean
    CommandLineRunner initBanques() {
        return args -> {

            if (banqueRepository.count() == 0) {

                banqueRepository.saveAll(List.of(
                        new Banque( "001", "Banque Populaire", "BCPOMAMCXXX"),
                        new Banque( "007", "Attijariwafa Bank", "BCMAMAMCXXX"),
                        new Banque( "011", "Bank of Africa", "BMCEMAMCXXX"),
                        new Banque( "230", "CIH Bank", "CIHMMAMCXXX"),
                        new Banque( "014", "BMCI", "BMCIMAMCXXX"),
                        new Banque( "015", "Crédit du Maroc", "CDMAMAMCXXX"),
                        new Banque( "021", "Société Générale Maroc", "SGMBMAMCXXX"),
                        new Banque( "022", "CFG Bank", "CAFGMAMCXXX"),
                        new Banque( "023", "Al Barid Bank", "ABBMMAMCXXX"),
                        new Banque( "024", "Umnia Bank", "UMNIMAMXXX")
                ));

                System.out.println("Jeu de données Banque chargé.");
            }
        };
    }
}