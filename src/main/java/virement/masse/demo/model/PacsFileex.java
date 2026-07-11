/*
 * package virement.masse.demo.model;
 * 
 * import java.util.ArrayList; import java.util.List;
 * 
 * import jakarta.persistence.Column; import jakarta.persistence.Entity; import
 * jakarta.persistence.Id; import jakarta.persistence.OneToMany; import
 * jakarta.persistence.Table; import jakarta.persistence.CascadeType; import
 * jakarta.persistence.FetchType;
 * 
 * @Entity
 * 
 * @Table(name = "pacs_files") public class PacsFileex {
 * 
 * @Id
 * 
 * @Column(name = "msg_id", length = 35) private String msgId;
 * 
 * private String fileName; private String creationDate; // CreDtTm private
 * Integer nbOfTxs; private Double ctrlSum; private String initiatingPartyName;
 * // InitgPty/Nm
 * 
 * @Column(name = "statut") private String statut; // --- Settlement Information
 * --- private String intrBkSttlmDt; // IntrBkSttlmDt private String sttlmMtd;
 * // SttlmMtd private String clrSysPrtry; // SttlmInf/ClrSys/Prtry
 * 
 * // --- Instructing Agent (InstgAgt) --- private String instgAgtBicfi; //
 * InstgAgt/FinInstnId/BICFI private String instgAgtClrSysPrtry; //
 * InstgAgt/FinInstnId/ClrSysMmbid/ClrSysid/Prtry private String instgAgtMmbId;
 * // InstgAgt/FinInstnId/ClrSysMmbId/MmbId
 * 
 * // --- Instructed Agent (InstdAgt) --- private String instdAgtBicfi; //
 * InstdAgt/FinInstnId/BICFI private String instdAgtClrSysPrtry; //
 * InstdAgt/FinInstnId/ClrSysMmbid/ClrSysid/Prtry private String instdAgtMmbId;
 * // InstdAgt/FinInstnId/ClrSysMmbId/MmbId
 * 
 * @OneToMany(mappedBy = "pacsFile", cascade = CascadeType.ALL, fetch =
 * FetchType.LAZY) private List<Pacs008Transaction> transactions = new
 * ArrayList<>();
 * 
 * // --- Constructeurs ---
 * 
 * // Constructeur par défaut (Obligatoire pour JPA/Hibernate) public PacsFile()
 * { }
 * 
 * // Constructeur utilisé par ton Parser : new PacsFile(file.getName()) public
 * PacsFile(String fileName) { this.fileName = fileName; }
 * 
 * // --- Getters et Setters ---
 * 
 * public String getMsgId() { return msgId; }
 * 
 * public void setMsgId(String msgId) { this.msgId = msgId; }
 * 
 * public String getStatut() { return statut; }
 * 
 * public void setStatut(String statut) { this.statut = statut; }
 * 
 * public String getFileName() { return fileName; }
 * 
 * public void setFileName(String fileName) { this.fileName = fileName; }
 * 
 * public String getCreationDate() { return creationDate; }
 * 
 * public void setCreationDate(String creationDate) { this.creationDate =
 * creationDate; }
 * 
 * public Integer getNbOfTxs() { return nbOfTxs; }
 * 
 * public void setNbOfTxs(Integer nbOfTxs) { this.nbOfTxs = nbOfTxs; }
 * 
 * public Double getCtrlSum() { return ctrlSum; }
 * 
 * public void setCtrlSum(Double ctrlSum) { this.ctrlSum = ctrlSum; }
 * 
 * public String getInitiatingPartyName() { return initiatingPartyName; }
 * 
 * public void setInitiatingPartyName(String initiatingPartyName) {
 * this.initiatingPartyName = initiatingPartyName; }
 * 
 * public String getIntrBkSttlmDt() { return intrBkSttlmDt; }
 * 
 * public void setIntrBkSttlmDt(String intrBkSttlmDt) { this.intrBkSttlmDt =
 * intrBkSttlmDt; }
 * 
 * public String getSttlmMtd() { return sttlmMtd; }
 * 
 * public void setSttlmMtd(String sttlmMtd) { this.sttlmMtd = sttlmMtd; }
 * 
 * public String getClrSysPrtry() { return clrSysPrtry; }
 * 
 * public void setClrSysPrtry(String clrSysPrtry) { this.clrSysPrtry =
 * clrSysPrtry; }
 * 
 * public String getInstgAgtBicfi() { return instgAgtBicfi; }
 * 
 * public void setInstgAgtBicfi(String instgAgtBicfi) { this.instgAgtBicfi =
 * instgAgtBicfi; }
 * 
 * public String getInstgAgtClrSysPrtry() { return instgAgtClrSysPrtry; }
 * 
 * public void setInstgAgtClrSysPrtry(String instgAgtClrSysPrtry) {
 * this.instgAgtClrSysPrtry = instgAgtClrSysPrtry; }
 * 
 * public String getInstgAgtMmbId() { return instgAgtMmbId; }
 * 
 * public void setInstgAgtMmbId(String instgAgtMmbId) { this.instgAgtMmbId =
 * instgAgtMmbId; }
 * 
 * public String getInstdAgtBicfi() { return instdAgtBicfi; }
 * 
 * public void setInstdAgtBicfi(String instdAgtBicfi) { this.instdAgtBicfi =
 * instdAgtBicfi; }
 * 
 * public String getInstdAgtClrSysPrtry() { return instdAgtClrSysPrtry; }
 * 
 * public void setInstdAgtClrSysPrtry(String instdAgtClrSysPrtry) {
 * this.instdAgtClrSysPrtry = instdAgtClrSysPrtry; }
 * 
 * public String getInstdAgtMmbId() { return instdAgtMmbId; }
 * 
 * public void setInstdAgtMmbId(String instdAgtMmbId) { this.instdAgtMmbId =
 * instdAgtMmbId; }
 * 
 * public List<Pacs008Transaction> getTransactions() { return transactions; }
 * 
 * public void setTransactions(List<Pacs008Transaction> transactions) {
 * this.transactions = transactions; } }
 */