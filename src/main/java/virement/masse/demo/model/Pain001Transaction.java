package virement.masse.demo.model;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "pain001_transactions")
public class Pain001Transaction {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 35)
	private String paymentInfoId;

	@Column(length = 35)
	private String endToEndId;

	@Column(length = 35)
	private String instructionId;

	@Column(precision = 18, scale = 2)
	private BigDecimal amount;

	@Column(length = 3)
	private String currency;

	@Column(length = 140)
	private String debtorName;

	@Column(length = 34)
	private String debtorIban;

	@Column(length = 140)
	private String creditorName;

	@Column(length = 34)
	private String creditorIban;

	@Column(length = 11)
	private String creditorBankBic;

	@Column(length = 3)
	private String creditorBankMemberId;

	@Column(length = 140)
	private String remittanceInfo;

	@Column(length = 140)
	private String MotifRejet;

	@Column(length = 30)
	private String statut = "RECU";

	@Column(length = 1000)
	private String errorMessage;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pain001_file_id")
	@JsonIgnore
	private Pain001File pain001File;
}