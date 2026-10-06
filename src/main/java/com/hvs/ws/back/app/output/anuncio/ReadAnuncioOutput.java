package com.hvs.ws.back.app.output.anuncio;

import com.hvs.ws.back.domain.entity.anuncio.Anuncio;

import java.math.BigDecimal;

/**
 * JSON no formato esperado pelo front ({@code Anuncio}): nomes simples,
 * sem o prefixo {@code a} usado nos demais módulos — já é assim no
 * <i>ad-slot</i> e no formulário da página <i>anuncie</i>.
 */
public record ReadAnuncioOutput(Long id,
                                String uuid,
                                String statusDesc,
                                String titulo,
                                String descricao,
                                String imageUrl,
                                String linkUrl,
                                String moeda,
                                String walletAddress,
                                BigDecimal valorPago,
                                String txHash,
                                Integer posicao,
                                Integer largura,
                                Integer altura) {

    public static ReadAnuncioOutput from(final Anuncio aAnuncio) {

        return new ReadAnuncioOutput(
                aAnuncio.getId() != null ? aAnuncio.getId().getValue() : null,
                aAnuncio.getUuid() != null ? aAnuncio.getUuid().getValue() : null,
                aAnuncio.getStatus() != null ? aAnuncio.getStatus().getDesc() : null,
                aAnuncio.getTitulo(),
                aAnuncio.getDescricao(),
                aAnuncio.getImageUrl(),
                aAnuncio.getLinkUrl(),
                aAnuncio.getMoeda(),
                aAnuncio.getWalletAddress(),
                aAnuncio.getValorPago() != null ? aAnuncio.getValorPago().stripTrailingZeros() : null,
                aAnuncio.getTxHash(),
                aAnuncio.getPosicao(),
                aAnuncio.getLargura(),
                aAnuncio.getAltura());
    }
}
