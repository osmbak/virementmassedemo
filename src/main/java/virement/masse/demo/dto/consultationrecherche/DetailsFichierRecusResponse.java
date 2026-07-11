package virement.masse.demo.dto.consultationrecherche;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class DetailsFichierRecusResponse {

	private String msgId;
	private String nomFichier;
	private String bicBanqueDonneurOrdre;
	private String ribDonneurOrdre;
	private BigDecimal montantLot;
	private Integer nombreTransactionsLot;
	private String deviseLot;
	private String dateExecution;
	private String statut;
	private String codeMotifRejet;
	private String libelleMotifRejet;

	private Integer nbTransactions;
	private BigDecimal montantTotal;
	private String dateReception;
}