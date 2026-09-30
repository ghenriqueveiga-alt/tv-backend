package com.hvs.ws.back.app.usecase.propaganda;

import com.hvs.ws.back.app.command.propaganda.DeletePropagandaCommand;
import com.hvs.ws.back.app.output.propaganda.DeletePropagandaOutput;
import com.hvs.ws.back.domain.entity.propaganda.Propaganda;
import com.hvs.ws.back.domain.entity.propaganda.PropagandaDomainGateway;
import com.hvs.ws.back.domain.entity.propaganda.PropagandaId;
import com.hvs.ws.back.domain.validation.notification.Notification;
import io.vavr.control.Either;
import jakarta.transaction.Transactional;

import java.util.Optional;
import static io.vavr.API.Try;

public class DeletePropagandaUseCaseImpl extends DeletePropagandaUseCase {

    private final PropagandaDomainGateway gateway;

    public DeletePropagandaUseCaseImpl(
            PropagandaDomainGateway gateway) {
        this.gateway = gateway;
    }

    @Override
    public Either<Notification, DeletePropagandaOutput> execute(DeletePropagandaCommand aIn) {

        if (aIn.aId() == null) {

            return Either.left(Notification
                    .create(new Error("'id' cannot be null")));
        }

        final Optional<Propaganda> propagandaDb = this.gateway.read(PropagandaId.from(aIn.aId()));

        if (propagandaDb.isEmpty()) {

            return Either.left(Notification
                    .create(new Error("The Commercial with id: " + aIn.aId() + " could not be found.")));
        }

        delete(propagandaDb.get());

        return Try(propagandaDb::get)
                .toEither()
                .bimap(Notification::create, DeletePropagandaOutput::from);
    }

    @Transactional
    private void delete(final Propaganda aPropaganda){

        this.gateway.delete(aPropaganda);
    }
}
