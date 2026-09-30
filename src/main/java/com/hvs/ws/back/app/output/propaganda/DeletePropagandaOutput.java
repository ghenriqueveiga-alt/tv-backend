package com.hvs.ws.back.app.output.propaganda;

import com.hvs.ws.back.domain.entity.propaganda.Propaganda;

public record DeletePropagandaOutput(Long aId,
                                     String aUuid,
                                     String aMessage) {

    public static DeletePropagandaOutput from(final Propaganda aPropaganda) {

        return new DeletePropagandaOutput(
                aPropaganda.getId().getValue(),
                aPropaganda.getUuid().getValue(),
                "The Commercial with id: " + aPropaganda.getUuid().getValue() + " has been successfully deleted.");
    }
}
