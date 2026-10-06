package com.hvs.ws.back.domain.entity.anuncio;

/**
 * Confere o {@code txHash} do anúncio na blockchain da moeda usada:
 * transação existe, está confirmada, paga para a carteira de recebimento
 * do site e cobre o valor do plano.
 */
public interface AnuncioPagamentoGateway {

    VerificacaoPagamento verificar(Anuncio aAnuncio);
}
