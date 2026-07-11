package virement.masse.demo.dto;


import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PacsTransactionResponse {

    private Long id;
    private String msgId;
    private String instrId;
    private String endToEndId;
    private String compteDebiteur;
    private String compteCrediteur;
    private BigDecimal montant;
    private String devise;
    private String statut;
    private String motifRejet;

    public PacsTransactionResponse(Long id, String msgId, String instrId, String endToEndId,
                                   String compteDebiteur, String compteCrediteur,
                                   BigDecimal montant, String devise, String statut,
                                   String motifRejet, LocalDateTime dateTraitement) {
        this.id = id;
        this.msgId = msgId;
        this.instrId = instrId;
        this.endToEndId = endToEndId;
        this.compteDebiteur = compteDebiteur;
        this.compteCrediteur = compteCrediteur;
        this.montant = montant;
        this.devise = devise;
        this.statut = statut;
        this.motifRejet = motifRejet;
    }

    public Long getId() {
        return id;
    }

    public String getMsgId() {
        return msgId;
    }

    public String getInstrId() {
        return instrId;
    }

    public String getEndToEndId() {
        return endToEndId;
    }

    public String getCompteDebiteur() {
        return compteDebiteur;
    }

    public String getCompteCrediteur() {
        return compteCrediteur;
    }

    public BigDecimal getMontant() {
        return montant;
    }

    public String getDevise() {
        return devise;
    }

    public String getStatut() {
        return statut;
    }

    public String getMotifRejet() {
        return motifRejet;
    }

   
}
