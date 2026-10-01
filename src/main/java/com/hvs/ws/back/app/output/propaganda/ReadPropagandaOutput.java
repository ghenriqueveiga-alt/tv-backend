package com.hvs.ws.back.app.output.propaganda;

import com.hvs.ws.back.domain.entity.propaganda.Propaganda;

public record ReadPropagandaOutput(Long aId,
                                   String aUuid,
                                   String aStatusCode,
                                   Long aBlocoId,
                                   String aPosicao,
                                   String aNome,
                                   Integer aDuracaoSeg,
                                   Long aArquivoId,
                                   String aArquivoNome,
                                   Integer aOrdem,
                                   Integer aPagina) {

    public static ReadPropagandaOutput from(final Propaganda aPropaganda) {

        return new ReadPropagandaOutput(
                aPropaganda.getId().getValue(),
                aPropaganda.getUuid().getValue(),
                aPropaganda.getStatus() != null ? aPropaganda.getStatus().getCode() : null,
                aPropaganda.getBloco() != null ? aPropaganda.getBloco().getId().getValue() : null,
                aPropaganda.getPosicao() != null ? aPropaganda.getPosicao().getDesc() : null,
                aPropaganda.getNome(),
                aPropaganda.getDuracaoSeg(),
                aPropaganda.getArquivo() != null ? aPropaganda.getArquivo().getId().getValue() : null,
                aPropaganda.getArquivo() != null ? aPropaganda.getArquivo().getNome() : null,
                aPropaganda.getOrdem(),
                aPropaganda.getPagina());
    }
}
