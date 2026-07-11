package virement.masse.demo.dto;


import java.time.LocalDateTime;

import lombok.Data;
@Data

public class PacsTransactionDTO {

    private Long id;
    private String msgId;

    private String endToEndId;
    private String txId;

    private Double amount;
    private String currency;

    private String debtorName;
    private String debtorIban;

    private String creditorName;
    private String creditorIban;

    private String remittanceInfo;

    private String statut;
    private String motifRejet;
    private LocalDateTime dateTraitement;

    public PacsTransactionDTO() {}

    public PacsTransactionDTO(
            Long id,
            String msgId,
            String endToEndId,
            String txId,
            Double amount,
            String currency,
            String debtorName,
            String debtorIban,
            String creditorName,
            String creditorIban,
            String remittanceInfo,
            String statut,
            String motifRejet,
            LocalDateTime dateTraitement
    ) {
        this.id = id;
        this.msgId = msgId;
        this.endToEndId = endToEndId;
        this.txId = txId;
        this.amount = amount;
        this.currency = currency;
        this.debtorName = debtorName;
        this.debtorIban = debtorIban;
        this.creditorName = creditorName;
        this.creditorIban = creditorIban;
        this.remittanceInfo = remittanceInfo;
        this.statut = statut;
        this.motifRejet = motifRejet;
        this.dateTraitement = dateTraitement;
    }

    // getters/setters
}