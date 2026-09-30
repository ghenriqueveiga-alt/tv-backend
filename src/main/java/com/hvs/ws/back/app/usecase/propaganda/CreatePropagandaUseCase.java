package com.hvs.ws.back.app.usecase.propaganda;

import com.hvs.ws.back.app.command.propaganda.CreatePropagandaCommand;
import com.hvs.ws.back.app.output.propaganda.CreatePropagandaOutput;
import com.hvs.ws.back.app.usecase.UseCase;
import com.hvs.ws.back.domain.validation.notification.Notification;
import io.vavr.control.Either;

public abstract class CreatePropagandaUseCase extends UseCase<CreatePropagandaCommand, Either<Notification, CreatePropagandaOutput>> {
}
