package io.maju.frosty.checkin;

import io.maju.frosty.ValueObject;
import io.maju.frosty.utils.IdUtils;

import java.math.BigDecimal;
import java.util.Objects;

public class CheckinOrderItem extends ValueObject {

    private final String id;
    private final String name;
    private final BigDecimal price;
    private final Integer quantity;
    private final String productId;

    private CheckinOrderItem(
            final String anId,
            final String aName,
            final BigDecimal aPrice,
            final Integer aQuantity,
            final String aProductId
    ) {
        this.id = Objects.requireNonNull(anId, "'id' should not be null");
        this.name = Objects.requireNonNull(aName, "'name' should not be null");
        this.price = Objects.requireNonNull(aPrice, "'price' should not be null");
        this.quantity = Objects.requireNonNull(aQuantity, "'quantity' should not be null");
        this.productId = Objects.requireNonNull(aProductId, "'productId' should not be null");
    }

    public static CheckinOrderItem with(
            final String aName,
            final BigDecimal aPrice,
            final Integer aQuantity,
            final String aProductId
    ) {
        return new CheckinOrderItem(IdUtils.uuid(), aName, aPrice, aQuantity, aProductId);
    }

    public static CheckinOrderItem with(
            final String anId,
            final String aName,
            final BigDecimal aPrice,
            final Integer aQuantity,
            final String aProductId
    ) {
        return new CheckinOrderItem(anId, aName, aPrice, aQuantity, aProductId);
    }

    public BigDecimal total() {
        return this.price.multiply(BigDecimal.valueOf(this.quantity));
    }

    public String id() {
        return id;
    }

    public String name() {
        return name;
    }

    public BigDecimal price() {
        return price;
    }

    public Integer quantity() {
        return quantity;
    }

    public String productId() {
        return productId;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final CheckinOrderItem that = (CheckinOrderItem) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
