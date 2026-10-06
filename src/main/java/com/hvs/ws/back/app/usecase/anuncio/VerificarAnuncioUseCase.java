package com.hvs.ws.back.app.usecase.anuncio;

import com.hvs.ws.back.app.command.anuncio.VerificarAnuncioCommand;
import com.hvs.ws.back.app.output.anuncio.VerificarAnuncioOutput;
import com.hvs.ws.back.app.usecase.UseCase;
import com.hvs.ws.back.domain.validation.notification.Notification;
import io.vavr.control.Either;

public abstract class VerificarAnuncioUseCase extends UseCase<VerificarAnuncioCommand, Either<Notification, VerificarAnuncioOutput>> {
}
