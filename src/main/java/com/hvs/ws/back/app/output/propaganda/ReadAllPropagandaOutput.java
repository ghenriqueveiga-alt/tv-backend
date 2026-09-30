package com.hvs.ws.back.app.output.propaganda;

import com.hvs.ws.back.domain.entity.propaganda.Propaganda;

import java.util.List;

public record ReadAllPropagandaOutput(List<ReadPropagandaOutput> aPropagandas) {

    public static ReadAllPropagandaOutput from(final List<Propaganda> aPropagandas) {

        return new ReadAllPropagandaOutput(
                aPropagandas.stream().map(ReadPropagandaOutput::from).toList());
    }
}
