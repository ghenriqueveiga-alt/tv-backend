package com.hvs.ws.back.infra.persistence.propaganda;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PropagandaJpaRepository extends JpaRepository<PropagandaEntity, Long>, JpaSpecificationExecutor<PropagandaEntity> {

    Optional<PropagandaEntity> findByUuid(String uuid);

    @Query("select p from PropagandaEntity p where p.bloco.id = :blocoId order by p.ordem asc")
    List<PropagandaEntity> findByBloco(@Param("blocoId") Long blocoId);

    @Query("select p from PropagandaEntity p order by p.bloco.id asc, p.posicaoDesc asc, p.ordem asc")
    List<PropagandaEntity> findAllOrdered();

    @Query("select coalesce(max(p.ordem), 0) from PropagandaEntity p " +
           "where p.bloco.id = :blocoId and p.posicaoDesc = :posicao")
    Integer maxOrdem(@Param("blocoId") Long blocoId, @Param("posicao") String posicao);
}
