package com.hvs.ws.back.app.usecase.anuncio;

import com.hvs.ws.back.app.command.anuncio.CreateAnuncioCommand;
import com.hvs.ws.back.app.output.anuncio.CreateAnuncioOutput;
import com.hvs.ws.back.app.usecase.UseCase;
import com.hvs.ws.back.domain.validation.notification.Notification;
import io.vavr.control.Either;

public abstract class CreateAnuncioUseCase extends UseCase<CreateAnuncioCommand, Either<Notification, CreateAnuncioOutput>> {
}
