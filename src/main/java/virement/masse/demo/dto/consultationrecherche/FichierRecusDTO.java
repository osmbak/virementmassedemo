package virement.masse.demo.dto.consultationrecherche;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class FichierRecusDTO {

	private String msgId;
	private String bicBanqueDonneurOrdre;
	private String ribDonneurOrdre;
	private BigDecimal montantLot;
	private Integer nombreTransactionsLot;
	private String deviseLot;
	private String dateExecution;
	private String statut;
	private String codeMotifRejet;
	private String libelleMotifRejet;
}