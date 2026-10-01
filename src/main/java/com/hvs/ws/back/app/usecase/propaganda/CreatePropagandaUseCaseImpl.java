package com.hvs.ws.back.app.usecase.propaganda;

import com.hvs.ws.back.app.command.propaganda.CreatePropagandaCommand;
import com.hvs.ws.back.app.output.propaganda.CreatePropagandaOutput;
import com.hvs.ws.back.domain.entity.propaganda.Propaganda;
import com.hvs.ws.back.domain.entity.propaganda.PropagandaDomainGateway;
import com.hvs.ws.back.domain.entity.propaganda.PropagandaPosicao;
import com.hvs.ws.back.domain.validation.notification.Notification;
import io.vavr.API;
import io.vavr.control.Either;
import jakarta.transaction.Transactional;
import static io.vavr.API.Try;

public class CreatePropagandaUseCaseImpl extends CreatePropagandaUseCase {

    private final PropagandaDomainGateway gateway;

    public CreatePropagandaUseCaseImpl(
            PropagandaDomainGateway gateway) {
        this.gateway = gateway;
    }

    @Override
    public Either<Notification, CreatePropagandaOutput> execute(CreatePropagandaCommand aIn) {

        final var notification = Notification.create();
        final PropagandaPosicao posicao = aIn.aPosicaoCode() != null
                ? PropagandaPosicao.findByCode(aIn.aPosicaoCode())
                : null;

        Integer ordem = aIn.aOrdem();

        if (ordem == null && aIn.aBlocoId() != null && posicao != null) {

            ordem = this.gateway.nextOrdem(aIn.aBlocoId(), posicao);
        }

        final var propaganda = Propaganda.create(aIn.aBlocoId(),
                                                 aIn.aPosicaoCode(),
                                                 aIn.aNome(),
                                                 aIn.aDuracaoSeg(),
                                                 aIn.aArquivoId(),
                                                 ordem,
                                                 aIn.aPagina());
        propaganda.validate(notification);

        return notification.hasError() ? API.Left(notification) : create(propaganda);
    }

    @Transactional
    private Either<Notification, CreatePropagandaOutput> create(final Propaganda aPropaganda){

        return Try(() -> this.gateway.create(aPropaganda))
                .toEither()
                .bimap(Notification::create, CreatePropagandaOutput::from);
    }
}
