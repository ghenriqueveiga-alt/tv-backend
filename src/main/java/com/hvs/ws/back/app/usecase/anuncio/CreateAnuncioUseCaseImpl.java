package com.hvs.ws.back.app.usecase.anuncio;

import com.hvs.ws.back.app.command.anuncio.CreateAnuncioCommand;
import com.hvs.ws.back.app.output.anuncio.CreateAnuncioOutput;
import com.hvs.ws.back.domain.entity.anuncio.Anuncio;
import com.hvs.ws.back.domain.entity.anuncio.AnuncioDomainGateway;
import com.hvs.ws.back.domain.validation.notification.Notification;
import io.vavr.API;
import io.vavr.control.Either;
import jakarta.transaction.Transactional;

import static io.vavr.API.Try;

public class CreateAnuncioUseCaseImpl extends CreateAnuncioUseCase {

    private final AnuncioDomainGateway gateway;

    public CreateAnuncioUseCaseImpl(AnuncioDomainGateway gateway) {

        this.gateway = gateway;
    }

    @Override
    public Either<Notification, CreateAnuncioOutput> execute(CreateAnuncioCommand aIn) {

        final var notification = Notification.create();

        // Entra como "pendente": só passa a ser exibido depois que o pagamento
        // for confirmado na blockchain e um moderador aprovar.
        final var anuncio = Anuncio.create(aIn.titulo(),
                                           aIn.descricao(),
                                           aIn.imageUrl(),
                                           aIn.linkUrl(),
                                           aIn.moeda(),
                                           aIn.walletAddress(),
                                           aIn.txHash(),
                                           aIn.posicao(),
                                           aIn.largura(),
                                           aIn.altura(),
                                           aIn.valorPago());
        anuncio.validate(notification);

        return notification.hasError() ? API.Left(notification) : create(anuncio);
    }

    @Transactional
    private Either<Notification, CreateAnuncioOutput> create(final Anuncio aAnuncio) {

        return Try(() -> this.gateway.create(aAnuncio))
                .toEither()
                .bimap(Notification::create, CreateAnuncioOutput::from);
    }
}
