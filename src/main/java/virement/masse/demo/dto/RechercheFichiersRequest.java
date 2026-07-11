package virement.masse.demo.dto;

import lombok.Data;

@Data
public class RechercheFichiersRequest {

    private String msgId;
    private String nomFichier;
    private String statut;
    private String dateDebut;
    private String dateFin;
}