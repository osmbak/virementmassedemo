package virement.masse.demo.dto;


import virement.masse.demo.model.RejectionReasonCode;

public class TxStatus {

    private String instrId;
    private String endToEndId;
    private String status; // ACCP / RJCT
    private RejectionReasonCode reason;
    
    
    
    
	public String getInstrId() {
		return instrId;
	}
	public void setInstrId(String instrId) {
		this.instrId = instrId;
	}
	public String getEndToEndId() {
		return endToEndId;
	}
	public void setEndToEndId(String endToEndId) {
		this.endToEndId = endToEndId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public RejectionReasonCode getReason() {
		return reason;
	}
	public void setReason(RejectionReasonCode reason) {
		this.reason = reason;
	}

}