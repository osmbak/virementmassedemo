package virement.masse.demo.dto.pain002;

import lombok.Data;
import java.util.HashMap;
import java.util.Map;

@Data
public class Pacs002StatusResult {

    private String originalPacs008MsgId;
    private String groupStatus;

    // key = EndToEndId, value = statut ACCP/RJCT
    private Map<String, String> transactionStatuses = new HashMap<>();
}