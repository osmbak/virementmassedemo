package virement.masse.demo.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Pain001FileResponse {

	private Long id;
	private String msgId;
	private String fileName;
	private LocalDateTime creationDate;
	private LocalDateTime receptionDate;
	private Integer nbOfTxs;
	private BigDecimal ctrlSum;
	private String debtorName;
	private String debtorIban;
	private String statut;
	private String errorMessage;

	public Pain001FileResponse(Long id, String msgId, String fileName, LocalDateTime creationDate,
			LocalDateTime receptionDate, Integer nbOfTxs, BigDecimal ctrlSum, String debtorName, String debtorIban,
			String statut, String errorMessage) {
		this.id = id;
		this.msgId = msgId;
		this.fileName = fileName;
		this.creationDate = creationDate;
		this.receptionDate = receptionDate;
		this.nbOfTxs = nbOfTxs;
		this.ctrlSum = ctrlSum;
		this.debtorName = debtorName;
		this.debtorIban = debtorIban;
		this.statut = statut;
		this.errorMessage = errorMessage;
	}

	public Long getId() {
		return id;
	}

	public String getMsgId() {
		return msgId;
	}

	public String getFileName() {
		return fileName;
	}

	public LocalDateTime getCreationDate() {
		return creationDate;
	}

	public LocalDateTime getReceptionDate() {
		return receptionDate;
	}

	public Integer getNbOfTxs() {
		return nbOfTxs;
	}

	public BigDecimal getCtrlSum() {
		return ctrlSum;
	}

	public String getDebtorName() {
		return debtorName;
	}

	public String getDebtorIban() {
		return debtorIban;
	}

	public String getStatut() {
		return statut;
	}

	public String getErrorMessage() {
		return errorMessage;
	}
}