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

        return Either.right(ReadAllPropagandaOutput.from(lista));
    }
}
