package com.hvs.ws.back.app.output.propaganda;

import com.hvs.ws.back.domain.entity.propaganda.Propaganda;

public record CreatePropagandaOutput(Long aId,
                                     String aUuid,
                                     String aMessage) {

    public static CreatePropagandaOutput from(final Propaganda aPropaganda) {

        return new CreatePropagandaOutput(
                aPropaganda.getId().getValue(),
                aPropaganda.getUuid().getValue(),
                "The Commercial with id: " + aPropaganda.getUuid().getValue() + " has been successfully created.");
    }
}
