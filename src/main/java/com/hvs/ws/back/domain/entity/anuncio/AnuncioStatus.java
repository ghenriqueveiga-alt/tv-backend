package com.hvs.ws.back.domain.entity.anuncio;

/**
 * Ciclo de vida do anúncio:
 *  PENDENTE → enviado pelo anunciante (aguardando confirmação da tx);
 *  PAGO     → pagamento confirmado na blockchain (aguardando moderação);
 *  ATIVO    → aprovado, aparece no site na posição comprada;
 *  REPROVADO→ recusado (ou removido depois de ativo).
 */
public enum AnuncioStatus {

    PENDENTE("PE", "Pending"),
    PAGO("PG", "Paid"),
    ATIVO("AT", "Active"),
    REPROVADO("RJ", "Rejected");

    private final String code;
    private final String desc;

    AnuncioStatus(final String aCode, final String aDesc) {

        this.code = aCode;
        this.desc = aDesc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static AnuncioStatus findByCode(final String aCode) {

        if (aCode == null) return null;

        for (final var status : values()) {
            if (aCode.equals(status.getCode())) return status;
        }

        return null;
    }

    public static AnuncioStatus findByDesc(final String aDesc) {

        if (aDesc == null) return null;

        for (final var status : values()) {
            if (aDesc.equals(status.getDesc())) return status;
        }

        return null;
    }
}
