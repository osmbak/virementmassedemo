package virement.masse.demo.dto.consultationrecherche;

import lombok.Data;

@Data
public class RechercheFichiersRecusRequest {

	private String msgId;
	private String nomFichier;
	private String codeBanque;
	private String dateReception;
	private String statut;
	private String dateDebut;
	private String dateFin;
}