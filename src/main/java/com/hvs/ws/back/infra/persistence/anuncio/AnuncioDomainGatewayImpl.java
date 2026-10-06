package com.hvs.ws.back.infra.persistence.anuncio;

import com.hvs.ws.back.domain.entity.anuncio.Anuncio;
import com.hvs.ws.back.domain.entity.anuncio.AnuncioDomainGateway;
import com.hvs.ws.back.domain.entity.anuncio.AnuncioId;

import java.util.List;
import java.util.Optional;

public class AnuncioDomainGatewayImpl implements AnuncioDomainGateway {

    private final AnuncioJpaRepository repository;

    public AnuncioDomainGatewayImpl(AnuncioJpaRepository repository) {

        this.repository = repository;
    }

    @Override
    public Anuncio create(Anuncio aAnuncio) {

        return this.repository.save(AnuncioEntity.from(aAnuncio)).toDomain();
    }

    @Override
    public Optional<Anuncio> read(AnuncioId aId) {

        return this.repository.findById(aId.getValue()).map(AnuncioEntity::toDomain);
    }

    @Override
    public List<Anuncio> readAll(Integer aPosicao) {

        final var lista = this.repository.findAllOrdered().stream()
                .map(AnuncioEntity::toDomain);

        return (aPosicao != null
                ? lista.filter(anuncio -> aPosicao.equals(anuncio.getPosicao()))
                : lista)
                .toList();
    }

    @Override
    public Anuncio patch(Anuncio aAnuncio) {

        return this.repository.save(AnuncioEntity.from(aAnuncio)).toDomain();
    }

    @Override
    public void delete(Anuncio aAnuncio) {

        this.repository.deleteById(aAnuncio.getId().getValue());
    }
}
