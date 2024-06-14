package io.maju.frosty.checkin;

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

public class CheckinOrder extends AggregateRoot<CheckinOrderID> {

    private List<CheckinOrderItem> items;
    private boolean canceled;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    private CheckinOrder(
            final CheckinOrderID anId,
            final List<CheckinOrderItem> items,
            final boolean isCanceled,
            final Instant aCreatedAt,
            final Instant aUpdatedAt,
            final Instant aDeletedAt
    ) {
        super(anId);
        this.items = items;
        this.canceled = isCanceled;
        this.createdAt = Objects.requireNonNull(aCreatedAt, "'createdAt' should not be null");
        this.updatedAt = Objects.requireNonNull(aUpdatedAt, "'updatedAt' should not be null");
        this.deletedAt = aDeletedAt;

        selfValidate();
    }

    public static CheckinOrder newOrder(
            final List<CheckinOrderItem> items,
            final boolean isCanceled
    ) {
        final var anId = CheckinOrderID.unique();
        final var now = InstantUtils.now();
        final var deletedAt = isCanceled ? now : null;
        return new CheckinOrder(anId, items, isCanceled, now, now, deletedAt);
    }

    public static CheckinOrder with(
            final CheckinOrderID anId,
            final List<CheckinOrderItem> items,
            final boolean isCanceled,
            final Instant aCreatedAt,
            final Instant aUpdatedAt,
            final Instant aDeletedAt
    ) {
        return new CheckinOrder(anId, items, isCanceled, aCreatedAt, aUpdatedAt, aDeletedAt);
    }

    public static CheckinOrder with(final CheckinOrder anCheckinOrder) {
        return new CheckinOrder(
                anCheckinOrder.id,
                anCheckinOrder.items,
                anCheckinOrder.canceled,
                anCheckinOrder.createdAt,
                anCheckinOrder.updatedAt,
                anCheckinOrder.deletedAt
        );
    }

    @Override
    public void validate(final ValidationHandler handler) {
        new CheckinOrderValidator(this, handler).validate();
    }

    public CheckinOrder update(
            final List<CheckinOrderItem> items,
            final boolean isCanceled
    ) {
        if (isCanceled) {
            cancel();
        } else {
            uncancel();
        }
        this.items = new ArrayList<>(items != null ? items : Collections.emptyList());
        this.updatedAt = InstantUtils.now();
        selfValidate();
        return this;
    }

    public CheckinOrder uncancel() {
        this.deletedAt = null;
        this.canceled = false;
        this.updatedAt = InstantUtils.now();
        return this;
    }

    public CheckinOrder cancel() {
        if (deletedAt() == null) {
            this.deletedAt = InstantUtils.now();
        }
        this.canceled = true;
        this.updatedAt = InstantUtils.now();
        return this;
    }

    public BigDecimal total() {
        return this.items.stream().
                map(CheckinOrderItem::total)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<CheckinOrderItem> items() {
        return items != null ? Collections.unmodifiableList(items) : null;
    }

    public boolean isCanceled() {
        return canceled;
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

    public CheckinOrder addOrderItem(final CheckinOrderItem anCheckinOrderItem) {
        if (anCheckinOrderItem == null) {
            return this;
        }
        this.items.add(anCheckinOrderItem);
        this.updatedAt = InstantUtils.now();

        selfValidate();
        return this;
    }

    public CheckinOrder addOrderItems(final List<CheckinOrderItem> checkinOrderItems) {
        if (checkinOrderItems == null || checkinOrderItems.isEmpty()) {
            return this;
        }
        this.items.addAll(checkinOrderItems);
        this.updatedAt = InstantUtils.now();

        selfValidate();
        return this;
    }

    public CheckinOrder removeOrderItem(final CheckinOrderItem anCheckinOrderItem) {
        if (anCheckinOrderItem == null) {
            return this;
        }
        this.items.remove(anCheckinOrderItem);
        this.updatedAt = InstantUtils.now();
        return this;
    }
}
