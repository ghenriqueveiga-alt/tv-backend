package com.hvs.ws.back.app.usecase.anuncio;

import com.hvs.ws.back.app.command.anuncio.VerificarAnuncioCommand;
import com.hvs.ws.back.app.output.anuncio.VerificarAnuncioOutput;
import com.hvs.ws.back.domain.entity.anuncio.Anuncio;
import com.hvs.ws.back.domain.entity.anuncio.AnuncioDomainGateway;
import com.hvs.ws.back.domain.entity.anuncio.AnuncioId;
import com.hvs.ws.back.domain.entity.anuncio.AnuncioPagamentoGateway;
import com.hvs.ws.back.domain.entity.anuncio.AnuncioStatus;
import com.hvs.ws.back.domain.validation.notification.Notification;
import io.vavr.control.Either;
import jakarta.transaction.Transactional;

import static io.vavr.API.Try;

/**
 * Consulta a blockchain e, se o pagamento estiver confirmado, promove o
 * anúncio de PENDENTE para PAGO (a ativação continua sendo manual do moderador).
 * Verificação negativa não é erro: volta com {@code verificado=false} e o motivo
 * para a tela de moderação mostrar.
 */
public class VerificarAnuncioUseCaseImpl extends VerificarAnuncioUseCase {

    private final AnuncioDomainGateway gateway;
    private final AnuncioPagamentoGateway pagamento;

    public VerificarAnuncioUseCaseImpl(final AnuncioDomainGateway gateway,
                                       final AnuncioPagamentoGateway pagamento) {

        this.gateway = gateway;
        this.pagamento = pagamento;
    }

    @Override
    public Either<Notification, VerificarAnuncioOutput> execute(VerificarAnuncioCommand aIn) {

        if (aIn.id() == null) {

            return Either.left(Notification
                    .create(new Error("'id' cannot be null")));
        }

        final var anuncioDb = this.gateway.read(AnuncioId.from(aIn.id()));

        if (anuncioDb.isEmpty()) {

            return Either.left(Notification
                    .create(new Error("The Ad with id: " + aIn.id() + " could not be found.")));
        }

        return Try(() -> this.pagamento.verificar(anuncioDb.get()))
                .toEither()
                .mapLeft(Notification::create)
                .map(verificacao -> promoverSeConfirmado(anuncioDb.get(), verificacao));
    }

    @Transactional
    private VerificarAnuncioOutput promoverSeConfirmado(final Anuncio aAnuncio,
                                                        final com.hvs.ws.back.domain.entity.anuncio.VerificacaoPagamento aVerificacao) {

        if (!aVerificacao.ok() || aAnuncio.getStatus() == AnuncioStatus.ATIVO) {
            return VerificarAnuncioOutput.from(aAnuncio, aVerificacao);
        }

        final var pago = Anuncio.patch(aAnuncio.getId().getValue(),
                                       null, null, null, null, null, null, null,
                                       AnuncioStatus.PAGO.getCode(),
                                       null, null, null, null,
                                       aAnuncio);

        return VerificarAnuncioOutput.from(this.gateway.patch(pago), aVerificacao);
    }
}
