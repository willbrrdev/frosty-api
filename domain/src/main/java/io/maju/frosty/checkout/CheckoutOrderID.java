package io.maju.frosty.checkout;

import io.maju.frosty.Identifier;

import java.util.Objects;
import java.util.UUID;

public class CheckoutOrderID extends Identifier {

    private final String value;

    public CheckoutOrderID(final String aValue) {
        Objects.requireNonNull(aValue);
        this.value = aValue;
    }

    public static CheckoutOrderID unique() {
        return new CheckoutOrderID(UUID.randomUUID().toString());
    }

    public static CheckoutOrderID from(final String anId) {
        return new CheckoutOrderID(anId);
    }

    public static CheckoutOrderID from() {
        return new CheckoutOrderID(UUID.randomUUID().toString());
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final var that = (CheckoutOrderID) o;
        return Objects.equals(getValue(), that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getValue());
    }
}
