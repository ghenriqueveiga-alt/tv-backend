package com.hvs.ws.back.app.command.anuncio;

import java.math.BigDecimal;

public record PatchAnuncioCommand(Long id,
                                  String titulo,
                                  String descricao,
                                  String imageUrl,
                                  String linkUrl,
                                  String moeda,
                                  String walletAddress,
                                  String txHash,
                                  String status,
                                  Integer posicao,
                                  Integer largura,
                                  Integer altura,
                                  BigDecimal valorPago) {

    public static PatchAnuncioCommand from(final Long aId,
                                           final PatchAnuncioCommand aInput) {

        return new PatchAnuncioCommand(
                aId,
                aInput.titulo,
                aInput.descricao,
                aInput.imageUrl,
                aInput.linkUrl,
                aInput.moeda,
                aInput.walletAddress,
                aInput.txHash,
                aInput.status,
                aInput.posicao,
                aInput.largura,
                aInput.altura,
                aInput.valorPago);
    }
}
