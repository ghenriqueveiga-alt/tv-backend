package com.hvs.ws.back.app.usecase.anuncio;

import com.hvs.ws.back.app.command.anuncio.PatchAnuncioCommand;
import com.hvs.ws.back.app.output.anuncio.PatchAnuncioOutput;
import com.hvs.ws.back.domain.entity.anuncio.Anuncio;
import com.hvs.ws.back.domain.entity.anuncio.AnuncioDomainGateway;
import com.hvs.ws.back.domain.entity.anuncio.AnuncioId;
import com.hvs.ws.back.domain.entity.anuncio.AnuncioStatus;
import com.hvs.ws.back.domain.validation.notification.Notification;
import io.vavr.control.Either;
import jakarta.transaction.Transactional;

import java.util.Optional;

import static io.vavr.API.Try;

public class PatchAnuncioUseCaseImpl extends PatchAnuncioUseCase {

    private final AnuncioDomainGateway gateway;

    public PatchAnuncioUseCaseImpl(AnuncioDomainGateway gateway) {

        this.gateway = gateway;
    }

    @Override
    public Either<Notification, PatchAnuncioOutput> execute(PatchAnuncioCommand aIn) {

        if (aIn.id() == null) {

            return Either.left(Notification
                    .create(new Error("'id' cannot be null")));
        }

        final Optional<Anuncio> anuncioDb = this.gateway.read(AnuncioId.from(aIn.id()));

        if (anuncioDb.isEmpty()) {

            return Either.left(Notification
                    .create(new Error("The Ad with id: " + aIn.id() + " could not be found.")));
        }

        if (aIn.status() != null && AnuncioStatus.findByCode(aIn.status()) == null) {

            return Either.left(Notification
                    .create(new Error("Unknown status: " + aIn.status())));
        }

        final var notification = Notification.create();
        final var anuncio = Anuncio.patch(aIn.id(),
                                          aIn.titulo(),
                                          aIn.descricao(),
                                          aIn.imageUrl(),
                                          aIn.linkUrl(),
                                          aIn.moeda(),
                                          aIn.walletAddress(),
                                          aIn.txHash(),
                                          aIn.status(),
                                          aIn.posicao(),
                                          aIn.largura(),
                                          aIn.altura(),
                                          aIn.valorPago(),
                                          anuncioDb.get());
        anuncio.validate(notification);

        return notification.hasError() ? Either.left(notification) : patch(anuncio);
    }

    @Transactional
    private Either<Notification, PatchAnuncioOutput> patch(final Anuncio aAnuncio) {

        return Try(() -> this.gateway.patch(aAnuncio))
                .toEither()
                .bimap(Notification::create, PatchAnuncioOutput::from);
    }
}
