package com.hvs.ws.back.domain.entity.propaganda;

/**
 * Ocupa a metade de cima (Topo) ou de baixo (Base) do tempo livre
 * de um bloco dentro da grade.
 */
public enum PropagandaPosicao {

    TOPO("TO", "Topo"),
    BASE("BA", "Base");

    private final String code;
    private final String desc;

    PropagandaPosicao(String aCode, String aDesc) {
        this.code = aCode;
        this.desc = aDesc;
    }

    public static PropagandaPosicao findByCode(String aCode) {

        if (aCode != null) {
            for (PropagandaPosicao posicao : values()) {
                if (aCode.equals(posicao.getCode())) {
                    return posicao;
                }
            }
        }

        return null;
    }

    public static PropagandaPosicao findByDesc(String aDesc) {

        if (aDesc != null) {
            for (PropagandaPosicao posicao : values()) {
                if (aDesc.equals(posicao.getDesc())) {
                    return posicao;
                }
            }
        }

        return null;
    }

    public String getCode() { return code; }
    public String getDesc() { return desc; }
}
