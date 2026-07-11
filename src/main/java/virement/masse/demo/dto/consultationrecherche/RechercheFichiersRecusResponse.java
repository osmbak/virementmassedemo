package virement.masse.demo.dto.consultationrecherche;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class RechercheFichiersRecusResponse {

	private List<FichierRecusDTO> content;

	private int page;
	private int size;
	private long totalElements;
	private int totalPages;
}