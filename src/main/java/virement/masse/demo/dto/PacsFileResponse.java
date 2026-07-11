package virement.masse.demo.dto;

public class PacsFileResponse {

    private String msgId;
    private String nomFichier;
    private String statut;
    private Integer nbTransactions;
    private Double montantTotal;
    private String dateReception;

    public PacsFileResponse(
            String msgId,
            String nomFichier,
            String statut,
            Integer nbTransactions,
            Double montantTotal,
            String dateReception
    ) {
        this.msgId = msgId;
        this.nomFichier = nomFichier;
        this.statut = statut;
        this.nbTransactions = nbTransactions;
        this.montantTotal = montantTotal;
        this.dateReception = dateReception;
    }

    public String getMsgId() {
        return msgId;
    }

    public String getNomFichier() {
        return nomFichier;
    }

    public String getStatut() {
        return statut;
    }

    public Integer getNbTransactions() {
        return nbTransactions;
    }

    public Double getMontantTotal() {
        return montantTotal;
    }

    public String getDateReception() {
        return dateReception;
    }
}