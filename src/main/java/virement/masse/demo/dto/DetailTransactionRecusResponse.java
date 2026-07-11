package virement.masse.demo.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class DetailTransactionRecusResponse {

	private String referenceRio;
	private String endToEndId;
	private String dateExecution;
	private BigDecimal montant;
	private String nomBeneficiaire;
	private String ribBeneficiaire;
	private String motifVirement;
	private String libelleTransaction;
	private String statut;
	private String codeMotifRejet;
	private String libelleMotifRejet;
}