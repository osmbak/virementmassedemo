/*
 * package virement.masse.demo.impl;
 * 
 * import virement.masse.demo.dto.PacsTransactionDTO; import
 * virement.masse.demo.model.Pacs008Transaction; import
 * virement.masse.demo.repository.PacsTransactionRepository; import
 * virement.masse.demo.service.PacsTransactionConsultationService; import
 * virement.masse.demo.recherche.PacsTransactionSpecification;
 * 
 * import org.springframework.data.domain.Page; import
 * org.springframework.data.domain.Pageable; import
 * org.springframework.stereotype.Service;
 * 
 * @Service public class PacsTransactionConsultationServiceImpl implements
 * PacsTransactionConsultationService {
 * 
 * private final PacsTransactionRepository pacsTransactionRepository;
 * 
 * public PacsTransactionConsultationServiceImpl(PacsTransactionRepository
 * pacsTransactionRepository) { this.pacsTransactionRepository =
 * pacsTransactionRepository; }
 * 
 * @Override public Page<PacsTransactionDTO> rechercherTransactions( String
 * msgId, String instrId, String endToEndId, String compteDebiteur, String
 * compteCrediteur, String statut, Pageable pageable ) { return
 * pacsTransactionRepository .findAll( PacsTransactionSpecification.rechercher(
 * msgId, instrId, endToEndId, compteDebiteur, compteCrediteur, statut ),
 * pageable ) .map(this::toDTO); }
 * 
 * private PacsTransactionDTO toDTO(Pacs008Transaction tx) { return new
 * PacsTransactionDTO( tx.getId(), tx.getPacsFile() != null ?
 * tx.getPacsFile().getMsgId() : null, tx.getEndToEndId(), tx.getTxId(),
 * tx.getAmount(), tx.getCurrency(), tx.getDebtorName(), tx.getDebtorIban(),
 * tx.getCreditorName(), tx.getCreditorIban(), tx.getRemittanceInfo(),
 * tx.getStatut(), tx.getMotifRejet(), tx.getDateTraitement() ); } }
 */