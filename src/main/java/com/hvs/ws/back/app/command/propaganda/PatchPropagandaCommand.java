package com.hvs.ws.back.app.command.propaganda;

public record PatchPropagandaCommand(Long aId,
                                     String aNome,
                                     Integer aDuracaoSeg,
                                     Integer aOrdem) {

    public static PatchPropagandaCommand from(final Long aId,
                                              final PatchPropagandaCommand aInput) {

        return new PatchPropagandaCommand(
                aId,
                aInput.aNome,
                aInput.aDuracaoSeg,
                aInput.aOrdem);
    }
}
