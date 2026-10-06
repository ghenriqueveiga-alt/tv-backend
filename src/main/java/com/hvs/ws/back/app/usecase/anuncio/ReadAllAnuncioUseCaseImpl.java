package com.hvs.ws.back.app.usecase.anuncio;

import com.hvs.ws.back.app.command.anuncio.ReadAllAnuncioCommand;
import com.hvs.ws.back.app.output.anuncio.ReadAllAnuncioOutput;
import com.hvs.ws.back.domain.entity.anuncio.Anuncio;
import com.hvs.ws.back.domain.entity.anuncio.AnuncioDomainGateway;
import com.hvs.ws.back.domain.entity.anuncio.AnuncioStatus;
import com.hvs.ws.back.domain.validation.notification.Notification;
import io.vavr.control.Either;

import java.util.List;

public class ReadAllAnuncioUseCaseImpl extends ReadAllAnuncioUseCase {

    private final AnuncioDomainGateway gateway;

    public ReadAllAnuncioUseCaseImpl(AnuncioDomainGateway gateway) {

        this.gateway = gateway;
    }

    @Override
    public Either<Notification, ReadAllAnuncioOutput> execute(ReadAllAnuncioCommand aIn) {

        // Com posição o chamador é o site (ad-slot): só pode ver anúncios ativos.
        List<Anuncio> lista = this.gateway.readAll(aIn.posicao());

        if (aIn.posicao() != null) {

            lista = lista.stream()
                    .filter(anuncio -> anuncio.getStatus() == AnuncioStatus.ATIVO)
                    .toList();
        }

        // Sem posição é a moderação pedindo; o filtro de status é opcional.
        if (aIn.status() != null) {

            final var status = AnuncioStatus.findByCode(aIn.status());

            if (status == null) {

                return Either.left(Notification
                        .create(new Error("Unknown status: " + aIn.status())));
            }

            lista = lista.stream()
                    .filter(anuncio -> anuncio.getStatus() == status)
                    .toList();
        }

        return Either.right(ReadAllAnuncioOutput.from(lista));
    }
}
