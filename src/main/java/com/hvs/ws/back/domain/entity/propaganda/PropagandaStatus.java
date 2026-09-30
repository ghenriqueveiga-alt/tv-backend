package com.hvs.ws.back.domain.entity.propaganda;

public enum PropagandaStatus {

    ACTIVE("AT", "Active"),
    INACTIVE("IN", "Inactive"),
    DELETED("DE", "Deleted");

    private final String code;
    private final String desc;

    PropagandaStatus(String aCode, String aDesc) {
        this.code = aCode;
        this.desc = aDesc;
    }

    public static PropagandaStatus findByCode(String aCode) {

        if (aCode != null) {
            for (PropagandaStatus status : values()) {
                if (aCode.equals(status.getCode())) {
                    return status;
                }
            }
        }

        return null;
    }

    public static PropagandaStatus findByDesc(String aDesc) {

        if (aDesc != null) {
            for (PropagandaStatus status : values()) {
                if (aDesc.equals(status.getDesc())) {
                    return status;
                }
            }
        }

        return null;
    }

    public String getCode() {
        return code;
    }
    public String getDesc() {
        return desc;
    }
}
