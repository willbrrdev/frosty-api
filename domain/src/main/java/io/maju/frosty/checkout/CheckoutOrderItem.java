package io.maju.frosty.checkout;

import io.maju.frosty.ValueObject;
import io.maju.frosty.utils.IdUtils;

import java.math.BigDecimal;
import java.util.Objects;

public class CheckoutOrderItem extends ValueObject {

    private final String id;
    private final BigDecimal price;
    private final Integer quantity;
    private final String productId;

    private CheckoutOrderItem(
            final String anId,
            final BigDecimal aPrice,
            final Integer aQuantity,
            final String aProductId
    ) {
        this.id = Objects.requireNonNull(anId);
        this.price = Objects.requireNonNull(aPrice);
        this.quantity = Objects.requireNonNull(aQuantity);
        this.productId = Objects.requireNonNull(aProductId);
    }

    public static CheckoutOrderItem with(
            final BigDecimal aPrice,
            final Integer aQuantity,
            final String aProductId
    ) {
        return new CheckoutOrderItem(IdUtils.uuid(), aPrice, aQuantity, aProductId);
    }

    public static CheckoutOrderItem with(
            final String anId,
            final BigDecimal aPrice,
            final Integer aQuantity,
            final String aProductId
    ) {
        return new CheckoutOrderItem(anId, aPrice, aQuantity, aProductId);
    }

    public String id() {
        return id;
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
        final CheckoutOrderItem that = (CheckoutOrderItem) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
