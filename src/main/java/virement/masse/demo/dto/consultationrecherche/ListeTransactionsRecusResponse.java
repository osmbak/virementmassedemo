package virement.masse.demo.dto.consultationrecherche;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ListeTransactionsRecusResponse {

	private List<TransactionRecusDTO> content;

	private int page;
	private int size;
	private long totalElements;
	private int totalPages;
}