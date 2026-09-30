package com.hvs.ws.back.domain.entity.propaganda;

import java.util.List;
import java.util.Optional;

public interface PropagandaDomainGateway {

    Propaganda create(Propaganda aPropaganda);

    Optional<Propaganda> read(PropagandaId aId);

    /** Todas as propagandas de um bloco (ordenadas por {@code ordem}); com
     *  {@code aBlocoId} nulo, todas as propagandas ativas de todos os blocos. */
    List<Propaganda> readAll(Long aBlocoId);

    /** Próxima posição livre na sequência (bloco + metade do tempo livre). */
    Integer nextOrdem(Long aBlocoId, PropagandaPosicao aPosicao);

    Propaganda patch(Propaganda aPropaganda);

    void delete(Propaganda aPropaganda);
}
