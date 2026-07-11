package virement.masse.demo.dto;

public class PacsFileDetailResponse {

    private String msgId;
    private String nomFichier;
    private Integer nbTransactions;
    private Double montantTotal;
    private String dateReception;

    public PacsFileDetailResponse(
            String msgId,
            String nomFichier,
            Integer nbTransactions,
            Double montantTotal,
            String dateReception
    ) {
        this.msgId = msgId;
        this.nomFichier = nomFichier;
        this.nbTransactions = nbTransactions;
        this.montantTotal = montantTotal;
        this.dateReception = dateReception;
    }

    public String getMsgId() { return msgId; }
    public String getNomFichier() { return nomFichier; }
    public Integer getNbTransactions() { return nbTransactions; }
    public Double getMontantTotal() { return montantTotal; }
    public String getDateReception() { return dateReception; }
}