package com.hvs.ws.back.app.command.propaganda;

public record CreatePropagandaCommand(Long aBlocoId,
                                      String aPosicaoCode,
                                      String aNome,
                                      Integer aDuracaoSeg,
                                      Long aArquivoId,
                                      Integer aOrdem) {

    public static CreatePropagandaCommand from(final Long aBlocoId,
                                               final String aPosicaoCode,
                                               final String aNome,
                                               final Integer aDuracaoSeg,
                                               final Long aArquivoId,
                                               final Integer aOrdem) {

        return new CreatePropagandaCommand(
                aBlocoId,
                aPosicaoCode,
                aNome,
                aDuracaoSeg,
                aArquivoId,
                aOrdem);
    }
}
