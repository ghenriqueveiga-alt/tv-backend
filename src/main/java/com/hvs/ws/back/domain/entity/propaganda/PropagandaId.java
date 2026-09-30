package com.hvs.ws.back.domain.entity.propaganda;

import com.hvs.ws.back.domain.entity.Identifier;
import java.util.Objects;

public class PropagandaId extends Identifier {

    private final Long value;

    private PropagandaId(final Long value) {

        this.value = value;
    }

    public static PropagandaId from(final Long aId) {

        return new PropagandaId(aId);

    }
    public Long getValue() { return value; }

    @Override
    public boolean equals(Object o) {

        if (o == null || getClass() != o.getClass())
            return false;

        PropagandaId propagandaId = (PropagandaId) o;

        return Objects.equals(value, propagandaId.value);
    }

    @Override
    public int hashCode() {

        return Objects.hashCode(value);
    }
}
