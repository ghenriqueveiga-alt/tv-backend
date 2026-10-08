package com.hvs.ws.back.infra.persistence.episodio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EpisodioJpaRepository extends JpaRepository<EpisodioEntity, Long>, JpaSpecificationExecutor<EpisodioEntity> {

    Optional<EpisodioEntity> findByUuid(String uuid);

    List<EpisodioEntity> findByProgramaId(Long programaId);

    @Query(value =
        "SELECT e.* FROM episodio e INNER JOIN (" +
        "  SELECT ep.id, ROW_NUMBER() OVER (PARTITION BY ep.programa_id ORDER BY ep.temporada, ep.parte, ep.numero) AS rn" +
        "  FROM episodio ep" +
        "  WHERE ep.programa_id IN (:programaIds) AND ep.status_desc IN ('Active', 'Created')" +
        ") sub ON e.id = sub.id WHERE sub.rn = 1",
        nativeQuery = true)
    List<EpisodioEntity> findFirstByProgramaIds(@Param("programaIds") List<Long> programaIds);

    @Query(value =
        "SELECT e.id, e.numero, e.titulo, e.programa_id, e.temporada, e.parte, e.duracao, e.capa_url, e.arquivo_id " +
        "FROM episodio e " +
        "INNER JOIN (" +
        "  SELECT ep.id, ROW_NUMBER() OVER (PARTITION BY ep.programa_id ORDER BY ep.temporada, ep.parte, ep.numero) AS rn" +
        "  FROM episodio ep" +
        "  WHERE ep.programa_id IN (:programaIds) AND ep.status_desc IN ('Active', 'Created')" +
        ") sub ON e.id = sub.id " +
        "WHERE sub.rn > :offset AND sub.rn <= :limite + :offset",
        nativeQuery = true)
    List<Object[]> findPrimeirosPorProgramaIds(@Param("programaIds") List<Long> programaIds, @Param("offset") int offset, @Param("limite") int limite);
}
