package virement.masse.demo.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pain001ProcessResponse {

    private String msgId;
    private String statut;
    private Integer nbOfTxs;
    private BigDecimal ctrlSum;
    private Integer nbPacs008Generated;
}