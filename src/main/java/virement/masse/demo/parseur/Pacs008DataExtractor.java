package virement.masse.demo.parseur;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import virement.masse.demo.model.Pacs008Transaction;
import virement.masse.demo.model.PacsFile;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class Pacs008DataExtractor {

	private enum Context {
		NONE,

		// Header
		STTLM, INSTG, INSTD, INITG,

		// Payment identification / type
		PMT_ID, PMT_TYPE_INFO, SVC_LVL, LCL_INSTRM, CTGY_PURP,

		// Debtor
		DBTR, DBTR_ID, DBTR_ACCT, DBTR_ACCT_TYPE, DBTR_AGENT,

		// Creditor
		CDTR, CDTR_ID, CDTR_ACCT, CDTR_ACCT_TYPE, CDTR_AGENT,

		// Remittance
		RMT_INF
	}

	@FunctionalInterface
	interface HeaderHandler {
		void handle(XMLStreamReader reader, PacsFile file, Context context) throws XMLStreamException;
	}

	@FunctionalInterface
	interface TxHandler {
		void handle(XMLStreamReader reader, Pacs008Transaction tx, Context context) throws XMLStreamException;
	}

	private final Map<String, HeaderHandler> headerHandlers = new HashMap<>();
	private final Map<String, TxHandler> txHandlers = new HashMap<>();

	@PostConstruct
	public void init() {

		initHeaderHandlers();
		initTransactionHandlers();
	}

	private void initHeaderHandlers() {

		headerHandlers.put("MsgId", (r, f, c) -> f.setMsgId(r.getElementText()));

		headerHandlers.put("CreDtTm",
				(r, f, c) -> f.setCreationDateTime(java.time.LocalDateTime.parse(r.getElementText())));

		headerHandlers.put("BtchBookg", (r, f, c) -> f.setBatchBooking(Boolean.valueOf(r.getElementText())));

		headerHandlers.put("NbOfTxs", (r, f, c) -> f.setNbOfTxs(Integer.parseInt(r.getElementText())));

		headerHandlers.put("CtrlSum", (r, f, c) -> f.setCtrlSum(Double.parseDouble(r.getElementText())));

		headerHandlers.put("TtlIntrBkSttlmAmt",
				(r, f, c) -> f.setTotalInterbankSettlementAmount(Double.parseDouble(r.getElementText())));

		headerHandlers.put("IntrBkSttlmDt",
				(r, f, c) -> f.setInterbankSettlementDate(java.time.LocalDate.parse(r.getElementText())));

		headerHandlers.put("SttlmMtd", (r, f, c) -> {
			if (c == Context.STTLM) {
				f.setSettlementMethod(r.getElementText());
			}
		});

		headerHandlers.put("ClrChanl", (r, f, c) -> f.setClearingChannel(r.getElementText()));

		headerHandlers.put("Nm", (r, f, c) -> {
			if (c == Context.INITG) {
				f.setInitiatingPartyName(r.getElementText());
			}
		});

		headerHandlers.put("BICFI", (r, f, c) -> {
			String bic = r.getElementText();

			if (c == Context.INSTG) {
				f.setInstgAgentBic(bic);
			} else if (c == Context.INSTD) {
				f.setInstdAgentBic(bic);
			}
		});

		headerHandlers.put("MmbId", (r, f, c) -> {
			String memberId = r.getElementText();

			if (c == Context.INSTG) {
				f.setInstgAgentMemberId(memberId);
			} else if (c == Context.INSTD) {
				f.setInstdAgentMemberId(memberId);
			}
		});
	}

	private void initTransactionHandlers() {

		/*
		 * Payment Identification
		 */
		txHandlers.put("InstrId", (r, tx, c) -> tx.setInstrId(r.getElementText()));

		txHandlers.put("EndToEndId", (r, tx, c) -> tx.setEndToEndId(r.getElementText()));

		txHandlers.put("TxId", (r, tx, c) -> tx.setTxId(r.getElementText()));

		txHandlers.put("UETR", (r, tx, c) -> tx.setUetr(r.getElementText()));

		/*
		 * Payment Type Information
		 */
		txHandlers.put("InstrPrty", (r, tx, c) -> tx.setInstructionPriority(r.getElementText()));

		txHandlers.put("ClrChanl", (r, tx, c) -> tx.setClearingChannel(r.getElementText()));

		txHandlers.put("Prtry", (r, tx, c) -> {
			String value = r.getElementText();

			if (c == Context.SVC_LVL) {
				tx.setServiceLevel(value);
			} else if (c == Context.LCL_INSTRM) {
				tx.setLocalInstrument(value);
			} else if (c == Context.CTGY_PURP) {
				tx.setCategoryPurpose(value);
			} else if (c == Context.DBTR_ACCT_TYPE) {
				tx.setDebtorAccountType(value);
			} else if (c == Context.CDTR_ACCT_TYPE) {
				tx.setCreditorAccountType(value);
			}
		});

		/*
		 * Amount
		 */
		txHandlers.put("IntrBkSttlmAmt", (r, tx, c) -> {
			tx.setCurrency(r.getAttributeValue(null, "Ccy"));
			tx.setAmount(Double.parseDouble(r.getElementText()));
		});

		/*
		 * Charges
		 */
		txHandlers.put("ChrgBr", (r, tx, c) -> tx.setChargeBearer(r.getElementText()));

		/*
		 * Names
		 */
		txHandlers.put("Nm", (r, tx, c) -> {
			String name = r.getElementText();

			if (c == Context.DBTR) {
				tx.setDebtorName(name);
			} else if (c == Context.CDTR) {
				tx.setCreditorName(name);
			}
		});

		/*
		 * Accounts
		 */
		txHandlers.put("IBAN", (r, tx, c) -> {
			String iban = r.getElementText();

			if (c == Context.DBTR_ACCT) {
				tx.setDebtorIban(iban);
			} else if (c == Context.CDTR_ACCT) {
				tx.setCreditorIban(iban);
			}
		});

		txHandlers.put("Othr", (r, tx, c) -> {
			/*
			 * On ne lit pas directement ici car Othr contient souvent Id. Le vrai
			 * traitement est fait dans le handler Id.
			 */
		});

		/*
		 * Identifications
		 */
		txHandlers.put("Id", (r, tx, c) -> {
			String id = r.getElementText();

			if (c == Context.DBTR_ID) {
				tx.setDebtorIdentification(id);
			} else if (c == Context.CDTR_ID) {
				tx.setCreditorIdentification(id);
			}
		});

		/*
		 * Country
		 */
		txHandlers.put("CtryOfRes", (r, tx, c) -> {
			String country = r.getElementText();

			if (c == Context.DBTR) {
				tx.setDebtorCountry(country);
			} else if (c == Context.CDTR) {
				tx.setCreditorCountry(country);
			}
		});

		/*
		 * Agents
		 */
		txHandlers.put("BICFI", (r, tx, c) -> {
			String bic = r.getElementText();

			if (c == Context.DBTR_AGENT) {
				tx.setDebtorAgentBic(bic);
			} else if (c == Context.CDTR_AGENT) {
				tx.setCreditorAgentBic(bic);
			}
		});

		txHandlers.put("MmbId", (r, tx, c) -> {
			String memberId = r.getElementText();

			if (c == Context.DBTR_AGENT) {
				tx.setDebtorAgentMemberId(memberId);
			} else if (c == Context.CDTR_AGENT) {
				tx.setCreditorAgentMemberId(memberId);
			}
		});

		/*
		 * Remittance
		 */
		txHandlers.put("Ustrd", (r, tx, c) -> tx.setRemittanceInfo(r.getElementText()));
	}

	public void fillHeaderData(XMLStreamReader reader, PacsFile pacsFile) throws XMLStreamException {

		log.debug("Extraction du Group Header...");

		Context context = Context.NONE;

		while (reader.hasNext()) {

			int event = reader.next();

			if (event == XMLStreamConstants.START_ELEMENT) {

				String tag = reader.getLocalName();

				context = updateHeaderContext(tag, context);

				HeaderHandler handler = headerHandlers.get(tag);

				if (handler != null) {
					handler.handle(reader, pacsFile, context);
				}

			} else if (event == XMLStreamConstants.END_ELEMENT) {

				String tag = reader.getLocalName();

				context = closeHeaderContext(tag, context);

				if ("GrpHdr".equals(tag)) {
					return;
				}
			}
		}
	}

	public void fillTransactionData(XMLStreamReader reader, Pacs008Transaction tx) throws XMLStreamException {

		log.debug("Extraction transaction PACS008...");

		Context context = Context.NONE;

		while (reader.hasNext()) {

			int event = reader.next();

			if (event == XMLStreamConstants.START_ELEMENT) {

				String tag = reader.getLocalName();

				context = updateTxContext(tag, context);

				TxHandler handler = txHandlers.get(tag);

				if (handler != null) {
					handler.handle(reader, tx, context);
				}

			} else if (event == XMLStreamConstants.END_ELEMENT) {

				String tag = reader.getLocalName();

				context = closeTxContext(tag, context);

				if ("CdtTrfTxInf".equals(tag)) {
					return;
				}
			}
		}
	}

	private Context updateHeaderContext(String tag, Context context) {

		return switch (tag) {
		case "SttlmInf" -> Context.STTLM;
		case "InstgAgt" -> Context.INSTG;
		case "InstdAgt" -> Context.INSTD;
		case "InitgPty" -> Context.INITG;
		default -> context;
		};
	}

	private Context closeHeaderContext(String tag, Context context) {

		return switch (tag) {
		case "SttlmInf", "InstgAgt", "InstdAgt", "InitgPty" -> Context.NONE;
		default -> context;
		};
	}

	private Context updateTxContext(String tag, Context context) {

		return switch (tag) {

		case "PmtId" -> Context.PMT_ID;
		case "PmtTpInf" -> Context.PMT_TYPE_INFO;
		case "SvcLvl" -> Context.SVC_LVL;
		case "LclInstrm" -> Context.LCL_INSTRM;
		case "CtgyPurp" -> Context.CTGY_PURP;

		case "Dbtr" -> Context.DBTR;
		case "DbtrAcct" -> Context.DBTR_ACCT;
		case "DbtrAgt" -> Context.DBTR_AGENT;

		case "Cdtr" -> Context.CDTR;
		case "CdtrAcct" -> Context.CDTR_ACCT;
		case "CdtrAgt" -> Context.CDTR_AGENT;

		case "RmtInf" -> Context.RMT_INF;

		case "Id" -> {
			if (context == Context.DBTR) {
				yield Context.DBTR_ID;
			} else if (context == Context.CDTR) {
				yield Context.CDTR_ID;
			} else {
				yield context;
			}
		}

		case "Tp" -> {
			if (context == Context.DBTR_ACCT) {
				yield Context.DBTR_ACCT_TYPE;
			} else if (context == Context.CDTR_ACCT) {
				yield Context.CDTR_ACCT_TYPE;
			} else {
				yield context;
			}
		}

		default -> context;
		};
	}

	private Context closeTxContext(String tag, Context context) {

		return switch (tag) {

		case "PmtId", "PmtTpInf", "SvcLvl", "LclInstrm", "CtgyPurp", "Dbtr", "DbtrAcct", "DbtrAgt", "Cdtr", "CdtrAcct",
				"CdtrAgt", "RmtInf" ->
			Context.NONE;

		case "Id" -> {
			if (context == Context.DBTR_ID) {
				yield Context.DBTR;
			} else if (context == Context.CDTR_ID) {
				yield Context.CDTR;
			} else {
				yield context;
			}
		}

		case "Tp" -> {
			if (context == Context.DBTR_ACCT_TYPE) {
				yield Context.DBTR_ACCT;
			} else if (context == Context.CDTR_ACCT_TYPE) {
				yield Context.CDTR_ACCT;
			} else {
				yield context;
			}
		}

		default -> context;
		};
	}
}