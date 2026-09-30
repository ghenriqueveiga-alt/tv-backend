package com.hvs.ws.back.domain.entity.propaganda;

import com.hvs.ws.back.domain.entity.Identifier;
import java.util.Objects;
import java.util.UUID;

public class PropagandaUuid extends Identifier {

    private final String value;

    private PropagandaUuid(final String aValue) {

        this.value = Objects.requireNonNull(aValue);
    }

    public static PropagandaUuid unique() {

        return new PropagandaUuid(UUID.randomUUID().toString().toLowerCase());

    }

    public static PropagandaUuid from(final String aId) {

        return new PropagandaUuid(aId);

    }

    public static PropagandaUuid from(final UUID aId) {

        return new PropagandaUuid(aId.toString().toLowerCase());

    }
    public String getValue() { return value; }

    @Override
    public boolean equals(Object o) {

        if (o == null || getClass() != o.getClass())
            return false;

        PropagandaUuid propagandaUuid = (PropagandaUuid) o;

        return Objects.equals(value, propagandaUuid.value);
    }

    @Override
    public int hashCode() {

        return Objects.hashCode(value);
    }
}
