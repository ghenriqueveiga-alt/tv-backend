package com.hvs.ws.back.app.usecase.anuncio;

import com.hvs.ws.back.app.command.anuncio.PatchAnuncioCommand;
import com.hvs.ws.back.app.output.anuncio.PatchAnuncioOutput;
import com.hvs.ws.back.app.usecase.UseCase;
import com.hvs.ws.back.domain.validation.notification.Notification;
import io.vavr.control.Either;

public abstract class PatchAnuncioUseCase extends UseCase<PatchAnuncioCommand, Either<Notification, PatchAnuncioOutput>> {
}
