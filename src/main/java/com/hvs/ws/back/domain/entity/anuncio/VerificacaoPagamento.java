package com.hvs.ws.back.domain.entity.anuncio;

/** Resultado da consulta do pagamento na blockchain. */
public record VerificacaoPagamento(boolean ok, String motivo) {

    public static VerificacaoPagamento confirmado(final String aMotivo) {

        return new VerificacaoPagamento(true, aMotivo);
    }

    public static VerificacaoPagamento pendente(final String aMotivo) {

        return new VerificacaoPagamento(false, aMotivo);
    }
}
