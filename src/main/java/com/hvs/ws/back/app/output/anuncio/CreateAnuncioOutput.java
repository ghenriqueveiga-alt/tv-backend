package com.hvs.ws.back.app.output.anuncio;

import com.hvs.ws.back.domain.entity.anuncio.Anuncio;

public record CreateAnuncioOutput(Long id,
                                  String uuid,
                                  String message) {

    public static CreateAnuncioOutput from(final Anuncio aAnuncio) {

        return new CreateAnuncioOutput(
                aAnuncio.getId().getValue(),
                aAnuncio.getUuid().getValue(),
                "The Ad with id: " + aAnuncio.getUuid().getValue() + " has been successfully created.");
    }
}
