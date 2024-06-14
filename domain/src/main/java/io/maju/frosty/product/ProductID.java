package io.maju.frosty.product;

import io.maju.frosty.Identifier;
import io.maju.frosty.utils.IdUtils;

import java.util.Objects;

public class ProductID extends Identifier {

    private final String value;

    private ProductID(final String value) {
        this.value = Objects.requireNonNull(value);
    }

    public static ProductID unique() {
        return ProductID.from(IdUtils.uuid());
    }

    public static ProductID from(final String anId) {
        return new ProductID(anId);
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final ProductID that = (ProductID) o;
        return getValue().equals(that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue());
    }

}
