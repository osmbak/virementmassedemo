package virement.masse.demo.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "pacs_file", uniqueConstraints = {
		@UniqueConstraint(name = "uk_pacs_file_msgid", columnNames = "msg_id") })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PacsFile {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/*
	 * ===== GROUP HEADER =====
	 */

	@Column(name = "msg_id", nullable = false)
	private String msgId;

	private LocalDateTime creationDateTime;

	private Boolean batchBooking;

	private Integer nbOfTxs;

	private Double ctrlSum;

	private Double totalInterbankSettlementAmount;

	private LocalDate interbankSettlementDate;

	private String settlementMethod;

	private String clearingChannel;

	/*
	 * ===== AGENTS =====
	 */

	private String instgAgentBic;

	private String instgAgentMemberId;

	private String instdAgentBic;

	private String instdAgentMemberId;

	/*
	 * ===== INITIATING PARTY =====
	 */

	private String initiatingPartyName;

	/*
	 * ===== STATUS =====
	 */

	@Enumerated(EnumType.STRING)
	private PacsStatus status;

	private String rejectionCode;

	private String rejectionReason;

	/*
	 * ===== FILE INFO =====
	 */

	private String fileName;

	private LocalDateTime receptionDate;

	/*
	 * ===== TRANSACTIONS =====
	 */

	@OneToMany(mappedBy = "pacsFile", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Pacs008Transaction> transactions;

	public PacsFile(String fileName) {
		this.fileName = fileName;
	}
}