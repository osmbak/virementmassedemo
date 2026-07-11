/*
 * package virement.masse.demo.impl;
 * 
 * import virement.masse.demo.dto.PacsFileDetailResponse; import
 * virement.masse.demo.dto.PacsFileResponse; import
 * virement.masse.demo.model.PacsFile; import
 * virement.masse.demo.repository.PacsFileRepository; import
 * virement.masse.demo.service.PacsFileConsultationService;
 * 
 * import org.springframework.data.domain.Page; import
 * org.springframework.data.domain.Pageable; import
 * org.springframework.stereotype.Service;
 * 
 * @Service public class PacsFileConsultationServiceImpl implements
 * PacsFileConsultationService {
 * 
 * private final PacsFileRepository pacsFileRepository;
 * 
 * public PacsFileConsultationServiceImpl(PacsFileRepository pacsFileRepository)
 * { this.pacsFileRepository = pacsFileRepository; }
 * 
 * @Override public Page<PacsFileResponse> rechercherFichiers( String msgId,
 * String nomFichier, String statut, String dateDebut, String dateFin, Pageable
 * pageable ) { return pacsFileRepository .findAll(pageable)
 * .map(this::toResponse); }
 * 
 * @Override public PacsFileDetailResponse consulterFichier(String msgId) {
 * 
 * PacsFile file = pacsFileRepository.findByMsgId(msgId) .orElseThrow(() -> new
 * RuntimeException("Fichier introuvable"));
 * 
 * return new PacsFileDetailResponse( file.getMsgId(), file.getFileName(),
 * file.getNbOfTxs(), file.getCtrlSum(), file.getCreationDate() ); }
 * 
 * private PacsFileResponse toResponse(PacsFile file) { return new
 * PacsFileResponse( file.getMsgId(), file.getFileName(), null,
 * file.getNbOfTxs(), file.getCtrlSum(), file.getCreationDate() ); } }
 */