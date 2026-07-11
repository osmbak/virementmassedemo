package virement.masse.demo.dto.pacs008recu;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PacsFileDTO {

	private String msgId;

	private Integer nbOfTxs;

	private Double ctrlSum;

	private String instgAgentBic;

	private String instdAgentBic;

	private String status;

	private String rejectionCode;

	private String rejectionReason;

	private String fileName;
}