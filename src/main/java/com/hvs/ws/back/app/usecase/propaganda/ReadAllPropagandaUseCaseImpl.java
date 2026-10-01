package com.hvs.ws.back.app.usecase.propaganda;

import com.hvs.ws.back.app.command.propaganda.ReadAllPropagandaCommand;
import com.hvs.ws.back.app.output.propaganda.ReadAllPropagandaOutput;
import com.hvs.ws.back.domain.entity.propaganda.Propaganda;
import com.hvs.ws.back.domain.entity.propaganda.PropagandaDomainGateway;
import com.hvs.ws.back.domain.entity.propaganda.PropagandaPosicao;
import com.hvs.ws.back.domain.validation.notification.Notification;
import io.vavr.control.Either;

import java.util.List;

public class ReadAllPropagandaUseCaseImpl extends ReadAllPropagandaUseCase {

    private final PropagandaDomainGateway gateway;

    public ReadAllPropagandaUseCaseImpl(
            PropagandaDomainGateway gateway) {
        this.gateway = gateway;
    }

    @Override
    public Either<Notification, ReadAllPropagandaOutput> execute(ReadAllPropagandaCommand aIn) {

        // Sem bloco: a grade pede tudo de uma vez para marcar os quadrados
        // "Livre" e os cards de intervalo sem fazer uma chamada por bloco.
        List<Propaganda> lista = this.gateway.readAll(aIn.aBlocoId());

        if (aIn.aPosicaoCode() != null) {

            final var posicao = PropagandaPosicao.findByCode(aIn.aPosicaoCode());

            if (posicao == null) {

                return Either.left(Notification
                        .create(new Error("Unknown position: " + aIn.aPosicaoCode())));
            }

            lista = lista.stream()
                    .filter(propaganda -> propaganda.getPosicao() == posicao)
                    .toList();
        }

        // Uma propaganda por página da grade: quem pede uma página só enxerga as
        // daquela página (sem página gravada = legado, vale para todas).
        if (aIn.aPagina() != null) {

            final Integer pagina = aIn.aPagina();

            lista = lista.stream()
                    .filter(propaganda -> propaganda.getPagina() == null
                            || pagina.equals(propaganda.getPagina()))
                    .toList();
        }

        return Either.right(ReadAllPropagandaOutput.from(lista));
    }
}
