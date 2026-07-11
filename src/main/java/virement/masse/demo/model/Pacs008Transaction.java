package virement.masse.demo.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "pacs_transaction", uniqueConstraints = {
		@UniqueConstraint(name = "uk_tx_endtoendid_file", columnNames = { "end_to_end_id", "pacs_file_id" }) })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pacs008Transaction {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/*
	 * ===== RELATION =====
	 */

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pacs_file_id")
	private PacsFile pacsFile;

	/*
	 * ===== PAYMENT IDENTIFICATION =====
	 */

	private String instrId;

	private String endToEndId;

	private String txId;

	private String uetr;

	/*
	 * ===== PAYMENT TYPE INFORMATION =====
	 */

	private String instructionPriority;

	private String clearingChannel;

	private String serviceLevel;

	private String localInstrument;

	private String categoryPurpose;

	/*
	 * ===== AMOUNT =====
	 */

	private Double amount;

	private String currency;

	/*
	 * ===== CHARGES =====
	 */

	private String chargeBearer;

	/*
	 * ===== DEBTOR =====
	 */

	private String debtorName;

	private String debtorCountry;

	private String debtorIdentification;

	private String debtorIban;

	private String debtorAccountType;

	private String debtorAgentBic;

	private String debtorAgentMemberId;

	/*
	 * ===== CREDITOR =====
	 */

	private String creditorName;

	private String creditorCountry;

	private String creditorIdentification;

	private String creditorIban;

	private String creditorAccountType;

	private String creditorAgentBic;

	private String creditorAgentMemberId;

	/*
	 * ===== REMITTANCE =====
	 */

	@Column(length = 2000)
	private String remittanceInfo;

	/*
	 * ===== STATUS =====
	 */

	@Enumerated(EnumType.STRING)
	private PacsStatus status;

	private String rejectionCode;

	private String rejectionReason;
}