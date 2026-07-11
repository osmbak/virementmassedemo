package virement.masse.demo.dto.pacs008recu;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PacsTransactionDTO {

	private String instrId;

	private String endToEndId;

	private String txId;

	private Double amount;

	private String currency;

	private String debtorName;

	private String debtorIban;

	private String creditorName;

	private String creditorIban;

	private String chargeBearer;

	private String remittanceInfo;

	private String status;

	private String rejectionCode;

	private String rejectionReason;
}