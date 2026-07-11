package virement.masse.demo.model;


public enum RejectionReasonCode {

    NARR("NARR"), // generic
    DUPL("DUPL"), // duplicate MsgId
    AM04("AM04"), // invalid amount
    FRMT("FRMT"), // format error
    CORE("CORE"); // business rule violation

    private final String code;

    RejectionReasonCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}