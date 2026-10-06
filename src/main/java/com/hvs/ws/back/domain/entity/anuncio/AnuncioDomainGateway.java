package com.hvs.ws.back.domain.entity.anuncio;

import java.util.List;
import java.util.Optional;

public interface AnuncioDomainGateway {

    Anuncio create(Anuncio aAnuncio);

    Optional<Anuncio> read(AnuncioId aId);

    /** Toda a carteira de anúncios; com posição informada, só os daquela posição. */
    List<Anuncio> readAll(Integer aPosicao);

    Anuncio patch(Anuncio aAnuncio);

    void delete(Anuncio aAnuncio);
}
