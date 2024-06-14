package io.maju.frosty.checkout;

import io.maju.frosty.AggregateRoot;
import io.maju.frosty.exceptions.NotificationException;
import io.maju.frosty.utils.InstantUtils;
import io.maju.frosty.validation.ValidationHandler;
import io.maju.frosty.validation.handler.Notification;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class CheckoutOrder extends AggregateRoot<CheckoutOrderID> {

    private BigDecimal amount;
    private List<CheckoutOrderItem> items;
    private boolean open;
    private CheckoutOrderStatus status;
    private String customerName;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    private CheckoutOrder(
            final CheckoutOrderID anId,
            final BigDecimal anAmount,
            final List<CheckoutOrderItem> items,
            final boolean isOpen,
            final CheckoutOrderStatus aStatus,
            final String aCustomerName,
            final Instant aCreatedAt,
            final Instant aUpdatedAt,
            final Instant aDeletedAt
    ) {
        super(anId);
        this.amount = anAmount;
        this.items = items;
        this.open = isOpen;
        this.status = aStatus;
        this.customerName = aCustomerName;
        this.createdAt = Objects.requireNonNull(aCreatedAt, "'createdAt' should not be null");
        this.updatedAt = Objects.requireNonNull(aUpdatedAt, "'updatedAt' should not be null");
        this.deletedAt = aDeletedAt;

        selfValidate();
    }

    public static CheckoutOrder newOrder(
            final BigDecimal anAmount,
            final List<CheckoutOrderItem> items,
            final boolean isOpen,
            final CheckoutOrderStatus aStatus,
            final String aCustomerName
    ) {
        final var anId = CheckoutOrderID.unique();
        final var now = InstantUtils.now();
        final var deletedAt = isOpen ? null : now;
        return new CheckoutOrder(anId, anAmount, items, isOpen, aStatus, aCustomerName, now, now, deletedAt);
    }

    public static CheckoutOrder with(
            final CheckoutOrderID anId,
            final BigDecimal anAmount,
            final List<CheckoutOrderItem> items,
            final boolean isOpen,
            final CheckoutOrderStatus aStatus,
            final String aCustomerName,
            final Instant aCreatedAt,
            final Instant aUpdatedAt,
            final Instant aDeletedAt
    ) {
        return new CheckoutOrder(anId, anAmount, items, isOpen, aStatus, aCustomerName, aCreatedAt, aUpdatedAt, aDeletedAt);
    }

    public static CheckoutOrder with(final CheckoutOrder anCheckoutOrder) {
        return new CheckoutOrder(
                anCheckoutOrder.id,
                anCheckoutOrder.amount,
                anCheckoutOrder.items,
                anCheckoutOrder.open,
                anCheckoutOrder.status,
                anCheckoutOrder.customerName,
                anCheckoutOrder.createdAt,
                anCheckoutOrder.updatedAt,
                anCheckoutOrder.deletedAt
        );
    }

    @Override
    public void validate(final ValidationHandler handler) {
        new CheckoutOrderValidator(this, handler).validate();
    }

    public CheckoutOrder update(
            final BigDecimal anAmount,
            final List<CheckoutOrderItem> items,
            final boolean isOpen,
            final CheckoutOrderStatus aStatus
    ) {
        if (isOpen) {
            open();
        } else {
            close(aStatus);
        }
        this.amount = anAmount;
        this.items = new ArrayList<>(items != null ? items : Collections.emptyList());
        this.updatedAt = InstantUtils.now();
        selfValidate();
        return this;
    }

    public CheckoutOrder open() {
        this.deletedAt = null;
        this.open = true;
        this.status = CheckoutOrderStatus.PENDING;
        this.updatedAt = InstantUtils.now();
        return this;
    }

    public CheckoutOrder close(final CheckoutOrderStatus newStatus) {
        if (deletedAt() == null) {
            this.deletedAt = InstantUtils.now();
        }
        this.open = false;
        this.status = newStatus;
        this.updatedAt = InstantUtils.now();

        selfValidate();
        return this;
    }

    public BigDecimal amount() {
        return amount;
    }

    public List<CheckoutOrderItem> items() {
        return items;
    }

    public CheckoutOrderStatus status() {
        return status;
    }

    public String customerName() {
        return customerName;
    }

    public boolean isOpen() {
        return open;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public Instant updatedAt() {
        return updatedAt;
    }

    public Instant deletedAt() {
        return deletedAt;
    }

    private void selfValidate() {
        final var notification = Notification.create();
        validate(notification);

        if (notification.hasError()) {
            throw new NotificationException("Failed to create a Aggregate Order", notification);
        }
    }

    public CheckoutOrder addOrderItem(final CheckoutOrderItem anCheckoutOrderItem) {
        if (anCheckoutOrderItem == null) {
            return this;
        }
        this.items.add(anCheckoutOrderItem);
        this.updatedAt = InstantUtils.now();
        return this;
    }

    public CheckoutOrder addOrderItems(final List<CheckoutOrderItem> checkoutOrderItems) {
        if (checkoutOrderItems == null || checkoutOrderItems.isEmpty()) {
            return this;
        }
        this.items.addAll(checkoutOrderItems);
        this.updatedAt = InstantUtils.now();
        return this;
    }

    public CheckoutOrder removeOrderItem(final CheckoutOrderItem anCheckoutOrderItem) {
        if (anCheckoutOrderItem == null) {
            return this;
        }
        this.items.remove(anCheckoutOrderItem);
        this.updatedAt = InstantUtils.now();
        return this;
    }
}
