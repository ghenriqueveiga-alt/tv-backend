package com.hvs.ws.back.app.command.propaganda;

public record DeletePropagandaCommand(Long aId) {

    public static DeletePropagandaCommand from(final Long aId) {

        return new DeletePropagandaCommand(aId);
    }
}
