package com.hvs.ws.back.app.command.propaganda;

public record ReadAllPropagandaCommand(Long aBlocoId,
                                       String aPosicaoCode) {

    public static ReadAllPropagandaCommand from(final Long aBlocoId,
                                                final String aPosicaoCode) {

        return new ReadAllPropagandaCommand(aBlocoId, aPosicaoCode);
    }
}
