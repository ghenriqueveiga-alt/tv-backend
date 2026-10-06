package com.hvs.ws.back.app.usecase.anuncio;

import com.hvs.ws.back.app.command.anuncio.DeleteAnuncioCommand;
import com.hvs.ws.back.app.output.anuncio.DeleteAnuncioOutput;
import com.hvs.ws.back.domain.entity.anuncio.Anuncio;
import com.hvs.ws.back.domain.entity.anuncio.AnuncioDomainGateway;
import com.hvs.ws.back.domain.entity.anuncio.AnuncioId;
import com.hvs.ws.back.domain.validation.notification.Notification;
import io.vavr.control.Either;
import jakarta.transaction.Transactional;

import java.util.Optional;

import static io.vavr.API.Try;

public class DeleteAnuncioUseCaseImpl extends DeleteAnuncioUseCase {

    private final AnuncioDomainGateway gateway;

    public DeleteAnuncioUseCaseImpl(AnuncioDomainGateway gateway) {

        this.gateway = gateway;
    }

    @Override
    public Either<Notification, DeleteAnuncioOutput> execute(DeleteAnuncioCommand aIn) {

        if (aIn.id() == null) {

            return Either.left(Notification
                    .create(new Error("'id' cannot be null")));
        }

        final Optional<Anuncio> anuncioDb = this.gateway.read(AnuncioId.from(aIn.id()));

        if (anuncioDb.isEmpty()) {

            return Either.left(Notification
                    .create(new Error("The Ad with id: " + aIn.id() + " could not be found.")));
        }

        return delete(anuncioDb.get());
    }

    @Transactional
    private Either<Notification, DeleteAnuncioOutput> delete(final Anuncio aAnuncio) {

        return Try(() -> {
                    this.gateway.delete(aAnuncio);
                    return aAnuncio;
                })
                .toEither()
                .bimap(Notification::create, DeleteAnuncioOutput::from);
    }
}
