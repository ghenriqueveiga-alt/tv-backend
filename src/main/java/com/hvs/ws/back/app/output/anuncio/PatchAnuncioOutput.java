package com.hvs.ws.back.app.output.anuncio;

import com.hvs.ws.back.domain.entity.anuncio.Anuncio;

public record PatchAnuncioOutput(Long id,
                                 String uuid,
                                 String message) {

    public static PatchAnuncioOutput from(final Anuncio aAnuncio) {

        return new PatchAnuncioOutput(
                aAnuncio.getId().getValue(),
                aAnuncio.getUuid().getValue(),
                "The Ad with id: " + aAnuncio.getUuid().getValue() + " has been successfully updated.");
    }
}
