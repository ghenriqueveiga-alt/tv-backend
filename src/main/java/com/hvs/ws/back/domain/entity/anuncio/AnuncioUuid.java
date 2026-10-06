package com.hvs.ws.back.domain.entity.anuncio;

import com.hvs.ws.back.domain.entity.Identifier;
import java.util.Objects;
import java.util.UUID;

public class AnuncioUuid extends Identifier {

    private final String value;

    private AnuncioUuid(final String aValue) {

        this.value = Objects.requireNonNull(aValue);
    }

    public static AnuncioUuid unique() {

        return new AnuncioUuid(UUID.randomUUID().toString().toLowerCase());
    }

    public static AnuncioUuid from(final String aId) {

        return new AnuncioUuid(aId);
    }

    public String getValue() { return value; }

    @Override
    public boolean equals(Object o) {

        if (o == null || getClass() != o.getClass())
            return false;

        AnuncioUuid anuncioUuid = (AnuncioUuid) o;

        return Objects.equals(value, anuncioUuid.value);
    }

    @Override
    public int hashCode() {

        return Objects.hashCode(value);
    }
}
