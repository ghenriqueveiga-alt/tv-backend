package com.hvs.ws.back.app.usecase.propaganda;

import com.hvs.ws.back.app.command.propaganda.PatchPropagandaCommand;
import com.hvs.ws.back.app.output.propaganda.PatchPropagandaOutput;
import com.hvs.ws.back.domain.entity.propaganda.Propaganda;
import com.hvs.ws.back.domain.entity.propaganda.PropagandaDomainGateway;
import com.hvs.ws.back.domain.entity.propaganda.PropagandaId;
import com.hvs.ws.back.domain.validation.notification.Notification;
import io.vavr.control.Either;
import jakarta.transaction.Transactional;

import java.util.Optional;
import static io.vavr.API.Try;

public class PatchPropagandaUseCaseImpl extends PatchPropagandaUseCase {

    private final PropagandaDomainGateway gateway;

    public PatchPropagandaUseCaseImpl(
            PropagandaDomainGateway gateway) {
        this.gateway = gateway;
    }

    @Override
    public Either<Notification, PatchPropagandaOutput> execute(PatchPropagandaCommand aIn) {

        if (aIn.aId() == null) {

            return Either.left(Notification
                    .create(new Error("'id' cannot be null")));
        }

        final Optional<Propaganda> propagandaDb = this.gateway.read(PropagandaId.from(aIn.aId()));

        if (propagandaDb.isEmpty()) {

            return Either.left(Notification
                    .create(new Error("The Commercial with id: " + aIn.aId() + " could not be found.")));
        }

        final var notification = Notification.create();
        final var propaganda = Propaganda.patch(aIn.aId(),
                                                aIn.aNome(),
                                                aIn.aDuracaoSeg(),
                                                aIn.aOrdem(),
                                                aIn.aArquivoId(),
                                                aIn.aRemoverArquivo(),
                                                aIn.aPagina(),
                                                propagandaDb.get());
        propaganda.validate(notification);

        return notification.hasError() ? Either.left(notification) : patch(propaganda);
    }

    @Transactional
    private Either<Notification, PatchPropagandaOutput> patch(final Propaganda aPropaganda){

        return Try(() -> this.gateway.patch(aPropaganda))
                .toEither()
                .bimap(Notification::create, PatchPropagandaOutput::from);
    }
}
