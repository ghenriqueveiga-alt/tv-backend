package com.hvs.ws.back.app.command.anuncio;

public record ReadAllAnuncioCommand(Integer posicao,
                                    String status) {

    public static ReadAllAnuncioCommand from(final Integer aPosicao,
                                             final String aStatus) {

        return new ReadAllAnuncioCommand(aPosicao, aStatus);
    }
}
