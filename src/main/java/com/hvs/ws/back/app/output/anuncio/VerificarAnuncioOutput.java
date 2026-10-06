package com.hvs.ws.back.app.output.anuncio;

import com.hvs.ws.back.domain.entity.anuncio.Anuncio;
import com.hvs.ws.back.domain.entity.anuncio.VerificacaoPagamento;

public record VerificarAnuncioOutput(Long id,
                                     String uuid,
                                     boolean verificado,
                                     String motivo,
                                     String status) {

    public static VerificarAnuncioOutput from(final Anuncio aAnuncio,
                                              final VerificacaoPagamento aVerificacao) {

        return new VerificarAnuncioOutput(
                aAnuncio.getId().getValue(),
                aAnuncio.getUuid().getValue(),
                aVerificacao.ok(),
                aVerificacao.motivo(),
                aAnuncio.getStatus() != null ? aAnuncio.getStatus().getCode() : null);
    }
}
