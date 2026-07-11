/*
 * package virement.masse.demo.model;
 * 
 * import jakarta.persistence.*;
 * 
 * import java.time.LocalDateTime;
 * 
 * @Entity
 * 
 * @Table(name = "pacs_transactions") public class Pacs008Transactionex {
 * 
 * @Id
 * 
 * @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
 * 
 * private String endToEndId; private String txId;
 * 
 * private Double amount; private String currency;
 * 
 * // Debtor private String debtorName; private String debtorIban;
 * 
 * // Creditor private String creditorName; private String creditorIban;
 * 
 * @Column(length = 140) private String remittanceInfo;
 * 
 * // ✅ AJOUT IMPORTANT private String statut; // ACCP / RJCT private String
 * motifRejet; // code rejet private LocalDateTime dateTraitement;
 * 
 * @ManyToOne(fetch = FetchType.LAZY)
 * 
 * @JoinColumn(name = "pacs_file_id") private PacsFile pacsFile;
 * 
 * public Pacs008Transaction() {}
 * 
 * // --- Getters / Setters ---
 * 
 * public Long getId() { return id; }
 * 
 * public String getEndToEndId() { return endToEndId; } public void
 * setEndToEndId(String endToEndId) { this.endToEndId = endToEndId; }
 * 
 * public String getTxId() { return txId; } public void setTxId(String txId) {
 * this.txId = txId; }
 * 
 * public Double getAmount() { return amount; } public void setAmount(Double
 * amount) { this.amount = amount; }
 * 
 * public String getCurrency() { return currency; } public void
 * setCurrency(String currency) { this.currency = currency; }
 * 
 * public String getDebtorName() { return debtorName; } public void
 * setDebtorName(String debtorName) { this.debtorName = debtorName; }
 * 
 * public String getDebtorIban() { return debtorIban; } public void
 * setDebtorIban(String debtorIban) { this.debtorIban = debtorIban; }
 * 
 * public String getCreditorName() { return creditorName; } public void
 * setCreditorName(String creditorName) { this.creditorName = creditorName; }
 * 
 * public String getCreditorIban() { return creditorIban; } public void
 * setCreditorIban(String creditorIban) { this.creditorIban = creditorIban; }
 * 
 * public String getRemittanceInfo() { return remittanceInfo; } public void
 * setRemittanceInfo(String remittanceInfo) { this.remittanceInfo =
 * remittanceInfo; }
 * 
 * public String getStatut() { return statut; } public void setStatut(String
 * statut) { this.statut = statut; }
 * 
 * public String getMotifRejet() { return motifRejet; } public void
 * setMotifRejet(String motifRejet) { this.motifRejet = motifRejet; }
 * 
 * public LocalDateTime getDateTraitement() { return dateTraitement; } public
 * void setDateTraitement(LocalDateTime dateTraitement) { this.dateTraitement =
 * dateTraitement; }
 * 
 * public PacsFile getPacsFile() { return pacsFile; } public void
 * setPacsFile(PacsFile pacsFile) { this.pacsFile = pacsFile; } }
 */