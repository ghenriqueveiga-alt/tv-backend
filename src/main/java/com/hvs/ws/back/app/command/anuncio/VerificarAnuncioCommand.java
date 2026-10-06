package com.hvs.ws.back.app.command.anuncio;

public record VerificarAnuncioCommand(Long id) {

    public static VerificarAnuncioCommand from(final Long aId) {

        return new VerificarAnuncioCommand(aId);
    }
}
