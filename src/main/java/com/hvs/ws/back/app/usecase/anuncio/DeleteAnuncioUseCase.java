package com.hvs.ws.back.app.usecase.anuncio;

import com.hvs.ws.back.app.command.anuncio.DeleteAnuncioCommand;
import com.hvs.ws.back.app.output.anuncio.DeleteAnuncioOutput;
import com.hvs.ws.back.app.usecase.UseCase;
import com.hvs.ws.back.domain.validation.notification.Notification;
import io.vavr.control.Either;

public abstract class DeleteAnuncioUseCase extends UseCase<DeleteAnuncioCommand, Either<Notification, DeleteAnuncioOutput>> {
}
