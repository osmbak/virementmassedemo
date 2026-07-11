package virement.masse.demo.model;


import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pacs008_generated_by_bank")
public class Pacs008GeneratedByBank {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String originalPainMsgId;

    private String generatedMsgId;

    private String creditorBankBic;

    private String creditorBankMemberId;

    private Integer nbOfTxs;

    private BigDecimal ctrlSum;

    private String currency;

    private String fileName;

    private String filePath;

    private LocalDateTime generationDate;

    private String statut; // GENERATED, ERROR, SENT

    @Column(length = 500)
    private String errorMessage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pain001_file_id")
    private Pain001File pain001File;

    public Pacs008GeneratedByBank() {
        this.generationDate = LocalDateTime.now();
        this.statut = "GENERATED";
    }

    public Long getId() {
        return id;
    }

    public String getOriginalPainMsgId() {
        return originalPainMsgId;
    }

    public void setOriginalPainMsgId(String originalPainMsgId) {
        this.originalPainMsgId = originalPainMsgId;
    }

    public String getGeneratedMsgId() {
        return generatedMsgId;
    }

    public void setGeneratedMsgId(String generatedMsgId) {
        this.generatedMsgId = generatedMsgId;
    }

    public String getCreditorBankBic() {
        return creditorBankBic;
    }

    public void setCreditorBankBic(String creditorBankBic) {
        this.creditorBankBic = creditorBankBic;
    }

    public String getCreditorBankMemberId() {
        return creditorBankMemberId;
    }

    public void setCreditorBankMemberId(String creditorBankMemberId) {
        this.creditorBankMemberId = creditorBankMemberId;
    }

    public Integer getNbOfTxs() {
        return nbOfTxs;
    }

    public void setNbOfTxs(Integer nbOfTxs) {
        this.nbOfTxs = nbOfTxs;
    }

    public BigDecimal getCtrlSum() {
        return ctrlSum;
    }

    public void setCtrlSum(BigDecimal ctrlSum) {
        this.ctrlSum = ctrlSum;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public LocalDateTime getGenerationDate() {
        return generationDate;
    }

    public void setGenerationDate(LocalDateTime generationDate) {
        this.generationDate = generationDate;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Pain001File getPain001File() {
        return pain001File;
    }

    public void setPain001File(Pain001File pain001File) {
        this.pain001File = pain001File;
    }
}