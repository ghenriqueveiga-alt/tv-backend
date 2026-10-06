package com.hvs.ws.back.app.command.anuncio;

public record DeleteAnuncioCommand(Long id) {

    public static DeleteAnuncioCommand from(final Long aId) {

        return new DeleteAnuncioCommand(aId);
    }
}
