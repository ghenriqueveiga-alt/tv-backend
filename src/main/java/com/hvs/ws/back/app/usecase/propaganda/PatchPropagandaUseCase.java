package com.hvs.ws.back.app.usecase.propaganda;

import com.hvs.ws.back.app.command.propaganda.PatchPropagandaCommand;
import com.hvs.ws.back.app.output.propaganda.PatchPropagandaOutput;
import com.hvs.ws.back.app.usecase.UseCase;
import com.hvs.ws.back.domain.validation.notification.Notification;
import io.vavr.control.Either;

public abstract class PatchPropagandaUseCase extends UseCase<PatchPropagandaCommand, Either<Notification, PatchPropagandaOutput>> {
}
