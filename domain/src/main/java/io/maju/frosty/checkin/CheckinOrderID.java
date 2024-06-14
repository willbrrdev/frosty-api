package io.maju.frosty.checkin;

import io.maju.frosty.Identifier;

import java.util.Objects;
import java.util.UUID;

public class CheckinOrderID extends Identifier {

    private final String value;

    public CheckinOrderID(final String aValue) {
        Objects.requireNonNull(aValue);
        this.value = aValue;
    }

    public static CheckinOrderID unique() {
        return new CheckinOrderID(UUID.randomUUID().toString());
    }

    public static CheckinOrderID from(final String anId) {
        return new CheckinOrderID(anId);
    }

    public static CheckinOrderID from() {
        return new CheckinOrderID(UUID.randomUUID().toString());
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final var that = (CheckinOrderID) o;
        return Objects.equals(getValue(), that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getValue());
    }
}
