package com.hvs.ws.back.app.usecase.anuncio;

import com.hvs.ws.back.app.command.anuncio.ReadAllAnuncioCommand;
import com.hvs.ws.back.app.output.anuncio.ReadAllAnuncioOutput;
import com.hvs.ws.back.app.usecase.UseCase;
import com.hvs.ws.back.domain.validation.notification.Notification;
import io.vavr.control.Either;

public abstract class ReadAllAnuncioUseCase extends UseCase<ReadAllAnuncioCommand, Either<Notification, ReadAllAnuncioOutput>> {
}
