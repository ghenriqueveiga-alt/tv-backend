package com.hvs.ws.back.app.usecase.propaganda;

import com.hvs.ws.back.app.command.propaganda.ReadAllPropagandaCommand;
import com.hvs.ws.back.app.output.propaganda.ReadAllPropagandaOutput;
import com.hvs.ws.back.app.usecase.UseCase;
import com.hvs.ws.back.domain.validation.notification.Notification;
import io.vavr.control.Either;

public abstract class ReadAllPropagandaUseCase extends UseCase<ReadAllPropagandaCommand, Either<Notification, ReadAllPropagandaOutput>> {
}
