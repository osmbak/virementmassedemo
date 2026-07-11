package virement.masse.demo.model;

import java.util.List;

import virement.masse.demo.dto.TxStatus;

public class Pacs002 {

	    private String msgId;              // ID du message original (PACS008)
	    private int nbOfTxs;               // Nombre de transactions
	    private PacsStatus status;         // ACCP / RJCT / PART

	    private List<TxStatus> txStatuses; // Détail par transaction

		public String getMsgId() {
			return msgId;
		}

		public void setMsgId(String msgId) {
			this.msgId = msgId;
		}

		public int getNbOfTxs() {
			return nbOfTxs;
		}

		public void setNbOfTxs(int nbOfTxs) {
			this.nbOfTxs = nbOfTxs;
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

	
}