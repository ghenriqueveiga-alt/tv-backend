package com.hvs.ws.back.infra.persistence.anuncio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AnuncioJpaRepository extends JpaRepository<AnuncioEntity, Long> {

    Optional<AnuncioEntity> findByUuid(String uuid);

    @Query("select a from AnuncioEntity a order by a.id desc")
    List<AnuncioEntity> findAllOrdered();
}
