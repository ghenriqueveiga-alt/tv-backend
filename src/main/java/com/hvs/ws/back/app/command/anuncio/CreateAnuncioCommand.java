package com.hvs.ws.back.app.command.anuncio;

import java.math.BigDecimal;

public record CreateAnuncioCommand(String titulo,
                                   String descricao,
                                   String imageUrl,
                                   String linkUrl,
                                   String moeda,
                                   String walletAddress,
                                   String txHash,
                                   Integer posicao,
                                   Integer largura,
                                   Integer altura,
                                   BigDecimal valorPago) {

    public static CreateAnuncioCommand from(final CreateAnuncioCommand aInput) {

        return new CreateAnuncioCommand(
                aInput.titulo,
                aInput.descricao,
                aInput.imageUrl,
                aInput.linkUrl,
                aInput.moeda,
                aInput.walletAddress,
                aInput.txHash,
                aInput.posicao,
                aInput.largura,
                aInput.altura,
                aInput.valorPago);
    }
}
