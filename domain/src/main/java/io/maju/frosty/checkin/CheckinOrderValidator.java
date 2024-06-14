package io.maju.frosty.checkin;

import io.maju.frosty.validation.Error;
import io.maju.frosty.validation.ValidationHandler;
import io.maju.frosty.validation.Validator;

import java.math.BigDecimal;

public class CheckinOrderValidator extends Validator {

    public static final int NAME_MIN_LENGTH = 3;
    public static final int NAME_MAX_LENGTH = 255;

    private final CheckinOrder checkinOrder;

    protected CheckinOrderValidator(final CheckinOrder anCheckinOrder, final ValidationHandler aHandler) {
        super(aHandler);
        this.checkinOrder = anCheckinOrder;
    }

    @Override
    public void validate() {
        checkItemsConstraints();
    }

    private void checkItemsConstraints() {
        final var items = this.checkinOrder.items();

        if (items == null) {
            this.validationHandler().append(new Error("'items' should not be null"));
            return;
        }

        if (items.isEmpty()) {
            this.validationHandler().append(new Error("'items' should not be empty"));
            return;
        }

        var index = 0;
        for (final var item : items) {
            checkItemConstraints(item, index);
            index++;
        }
    }

    private void checkItemConstraints(final CheckinOrderItem item, final int index) {

        if (item == null) {
            this.validationHandler().append(new Error("'item[%d]' should not be null".formatted(index)));
            return;
        }

        if (item.name().isBlank()) {
            this.validationHandler().append(new Error("'item[%d].name' should not be empty".formatted(index)));
            return;
        }

        final int length = item.name().trim().length();
        if (length > NAME_MAX_LENGTH || length < NAME_MIN_LENGTH) {
            this.validationHandler()
                    .append(new Error("'item[%d].name' must be between 3 and 255 characters".formatted(index)));
            return;
        }

        if (item.price().compareTo(BigDecimal.ZERO) <= 0) {
            this.validationHandler()
                    .append(new Error("'item[%d].price' should be greater than or equal to zero".formatted(index)));
            return;
        }

        if (item.quantity() <= 0) {
            this.validationHandler()
                    .append(new Error("'item[%d].quantity' should be greater than or equal to zero".formatted(index)));
            return;
        }

        if (item.productId().isBlank()) {
            this.validationHandler().append(new Error("'item[%d].productId' should not be empty".formatted(index)));
        }
    }
}
