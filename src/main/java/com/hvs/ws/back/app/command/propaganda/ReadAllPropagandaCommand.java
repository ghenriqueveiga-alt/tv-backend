package com.hvs.ws.back.app.command.propaganda;

public record ReadAllPropagandaCommand(Long aBlocoId,
                                       String aPosicaoCode,
                                       Integer aPagina) {

    public static ReadAllPropagandaCommand from(final Long aBlocoId,
                                                final String aPosicaoCode,
                                                final Integer aPagina) {

        return new ReadAllPropagandaCommand(aBlocoId, aPosicaoCode, aPagina);
    }
}
