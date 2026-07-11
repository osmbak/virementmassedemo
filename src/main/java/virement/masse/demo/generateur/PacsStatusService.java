package virement.masse.demo.generateur;

import org.springframework.stereotype.Service;

@Service
public class PacsStatusService {

	public boolean isAccepted(String msgId, int txCount) {

		// EXEMPLE DE RÈGLES MÉTIER

		if (msgId == null || msgId.isEmpty()) {
			return false;
		}

		if (txCount <= 0) {
			return false;
		}

		return true;
	}
	
}