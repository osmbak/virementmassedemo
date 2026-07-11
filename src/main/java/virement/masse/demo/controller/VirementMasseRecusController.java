package virement.masse.demo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import virement.masse.demo.dto.consultationrecherche.DetailTransactionRecusRequest;
import virement.masse.demo.dto.consultationrecherche.DetailTransactionRecusResponse;
import virement.masse.demo.dto.consultationrecherche.DetailsFichierRecusRequest;
import virement.masse.demo.dto.consultationrecherche.DetailsFichierRecusResponse;
import virement.masse.demo.dto.consultationrecherche.ListeTransactionsRecusRequest;
import virement.masse.demo.dto.consultationrecherche.ListeTransactionsRecusResponse;
import virement.masse.demo.dto.consultationrecherche.RechercheFichiersRecusRequest;
import virement.masse.demo.dto.consultationrecherche.RechercheFichiersRecusResponse;

import virement.masse.demo.service.VirementMasseRecusService;

@RestController
@RequestMapping("/api/v1/virements-masse")
@RequiredArgsConstructor
public class VirementMasseRecusController {

	private final VirementMasseRecusService service;

	@PostMapping("/RechercheFichiersRecus")
	public RechercheFichiersRecusResponse rechercheFichiersRecus(@RequestBody RechercheFichiersRecusRequest request) {
		return service.rechercheFichiersRecus(request);
	}

	@PostMapping("/DetailsFichierRecus")
	public DetailsFichierRecusResponse detailsFichierRecus(@RequestBody DetailsFichierRecusRequest request) {
		return service.detailsFichierRecus(request);
	}

	@PostMapping("/ListeTransactionsRecus")
	public ListeTransactionsRecusResponse listeTransactionsRecus(@RequestBody ListeTransactionsRecusRequest request) {
		return service.listeTransactionsRecus(request);
	}

	@PostMapping("/DetailTransactionsRecus")
	public DetailTransactionRecusResponse detailTransactionRecus(@RequestBody DetailTransactionRecusRequest request) {
		return service.detailTransactionRecus(request);
	}
}