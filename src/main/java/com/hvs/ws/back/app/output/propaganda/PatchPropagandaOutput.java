package com.hvs.ws.back.app.output.propaganda;

import com.hvs.ws.back.domain.entity.propaganda.Propaganda;

public record PatchPropagandaOutput(Long aId,
                                    String aUuid,
                                    String aMessage) {

    public static PatchPropagandaOutput from(final Propaganda aPropaganda) {

        return new PatchPropagandaOutput(
                aPropaganda.getId().getValue(),
                aPropaganda.getUuid().getValue(),
                "The Commercial with id: " + aPropaganda.getUuid().getValue() + " has been successfully updated.");
    }
}
