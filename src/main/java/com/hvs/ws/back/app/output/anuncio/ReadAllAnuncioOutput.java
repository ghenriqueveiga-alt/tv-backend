package com.hvs.ws.back.app.output.anuncio;

import com.hvs.ws.back.domain.entity.anuncio.Anuncio;

import java.util.List;

public record ReadAllAnuncioOutput(List<ReadAnuncioOutput> anuncios) {

    public static ReadAllAnuncioOutput from(final List<Anuncio> aAnuncios) {

        return new ReadAllAnuncioOutput(
                aAnuncios.stream().map(ReadAnuncioOutput::from).toList());
    }
}
