package virement.masse.demo.dto;


import virement.masse.demo.dto.TxStatus;
import virement.masse.demo.model.PacsStatus;

import java.util.List;

public class ParseResult {

    private String msgId;
    private PacsStatus status;
    private List<TxStatus> txStatuses;
	public String getMsgId() {
		return msgId;
	}
	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}
	public PacsStatus getStatus() {
		return status;
	}
	public void setStatus(PacsStatus status) {
		this.status = status;
	}
	public List<TxStatus> getTxStatuses() {
		return txStatuses;
	}
	public void setTxStatuses(List<TxStatus> txStatuses) {
		this.txStatuses = txStatuses;
	}

    // getters + setters
}
