package com.hvs.ws.back.app.usecase.propaganda;

import com.hvs.ws.back.app.command.propaganda.DeletePropagandaCommand;
import com.hvs.ws.back.app.output.propaganda.DeletePropagandaOutput;
import com.hvs.ws.back.app.usecase.UseCase;
import com.hvs.ws.back.domain.validation.notification.Notification;
import io.vavr.control.Either;

public abstract class DeletePropagandaUseCase extends UseCase<DeletePropagandaCommand, Either<Notification, DeletePropagandaOutput>> {
}
