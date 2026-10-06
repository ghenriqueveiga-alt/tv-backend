package com.hvs.ws.back.domain.entity.anuncio;

import com.hvs.ws.back.domain.entity.Identifier;
import java.util.Objects;

public class AnuncioId extends Identifier {

    private final Long value;

    private AnuncioId(final Long value) {

        this.value = value;
    }

    public static AnuncioId from(final Long aId) {

        return new AnuncioId(aId);
    }

    public Long getValue() { return value; }

    @Override
    public boolean equals(Object o) {

        if (o == null || getClass() != o.getClass())
            return false;

        AnuncioId anuncioId = (AnuncioId) o;

        return Objects.equals(value, anuncioId.value);
    }

    @Override
    public int hashCode() {

        return Objects.hashCode(value);
    }
}
