package com.hvs.ws.back.infra.persistence.propaganda;

import com.hvs.ws.back.domain.entity.propaganda.Propaganda;
import com.hvs.ws.back.domain.entity.propaganda.PropagandaDomainGateway;
import com.hvs.ws.back.domain.entity.propaganda.PropagandaId;
import com.hvs.ws.back.domain.entity.propaganda.PropagandaPosicao;
import com.hvs.ws.back.domain.entity.propaganda.PropagandaStatus;

import java.util.List;
import java.util.Optional;

public class PropagandaDomainGatewayImpl implements PropagandaDomainGateway {

    private final PropagandaJpaRepository repository;

    public PropagandaDomainGatewayImpl(PropagandaJpaRepository repository) {

        this.repository = repository;
    }

    @Override
    public Propaganda create(Propaganda aPropaganda) {

        return this.repository.save(PropagandaEntity.from(aPropaganda)).toDomain();
    }

    @Override
    public Optional<Propaganda> read(PropagandaId aId) {

        return this.repository.findById(aId.getValue()).map(PropagandaEntity::toDomain);
    }

    @Override
    public List<Propaganda> readAll(Long aBlocoId) {

        // Sem bloco: todas as propagandas ativas (a grade usa isso para marcar
        // os quadrados "Livre" e os cards de intervalo de uma só vez).
        final var entidades = aBlocoId == null
                ? this.repository.findAllOrdered()
                : this.repository.findByBloco(aBlocoId);

        return entidades.stream()
                .map(PropagandaEntity::toDomain)
                .filter(propaganda -> propaganda.getStatus() != PropagandaStatus.DELETED)
                .toList();
    }

    @Override
    public Integer nextOrdem(Long aBlocoId, PropagandaPosicao aPosicao) {

        if (aBlocoId == null || aPosicao == null) {
            return 1;
        }

        final Integer maxOrdem = this.repository.maxOrdem(aBlocoId, aPosicao.getDesc());

        return (maxOrdem != null ? maxOrdem : 0) + 1;
    }

    @Override
    public Propaganda patch(Propaganda aPropaganda) {

        return this.repository.save(PropagandaEntity.from(aPropaganda)).toDomain();
    }

    @Override
    public void delete(Propaganda aPropaganda) {

        final var entity = PropagandaEntity.from(aPropaganda);
        entity.setStatusDesc("Deleted");
        this.repository.save(entity);
    }
}
