package io.maju.frosty.checkout;

import io.maju.frosty.validation.Error;
import io.maju.frosty.validation.ValidationHandler;
import io.maju.frosty.validation.Validator;

import java.math.BigDecimal;

public class CheckoutOrderValidator extends Validator {

    public static final int NAME_MIN_LENGTH = 3;
    public static final int NAME_MAX_LENGTH = 255;

    private final CheckoutOrder checkoutOrder;

    protected CheckoutOrderValidator(final CheckoutOrder anCheckoutOrder, final ValidationHandler aHandler) {
        super(aHandler);
        this.checkoutOrder = anCheckoutOrder;
    }

    @Override
    public void validate() {
        checkItemsConstraints();
        checkAmountConstraints();
        checkOpenClosedConstraints();
        checkCustomerNameConstraints();
    }

    private void checkItemsConstraints() {
        final var items = this.checkoutOrder.items();

        if (items == null) {
            this.validationHandler().append(new Error("'items' should not be null"));
            return;
        }

        if (items.isEmpty()) {
            this.validationHandler().append(new Error("'items' should not be empty"));
        }
    }

    private void checkAmountConstraints() {
        final var amount = this.checkoutOrder.amount();

        if (amount == null) {
            this.validationHandler().append(new Error("'amount' should not be null"));
            return;
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            this.validationHandler().append(new Error("'amount' should be greater than zero"));
        }
    }

    private void checkCustomerNameConstraints() {
        final var customerName = this.checkoutOrder.customerName();
        if (customerName != null) {
            if (customerName.isBlank()) {
                this.validationHandler().append(new Error("'customerName' should not be empty"));
                return;
            }

            if (customerName.isEmpty()) {
                this.validationHandler().append(new Error("'customerName' should not be empty"));
                return;
            }

            final int length = customerName.trim().length();
            if (length > NAME_MAX_LENGTH || length < NAME_MIN_LENGTH) {
                this.validationHandler().append(new Error("'customerName' must be between 3 and 255 characters"));
            }
        }
    }

    private void checkOpenClosedConstraints() {
        final var isOpen = this.checkoutOrder.isOpen();
        final var status = this.checkoutOrder.status();
        if (!isOpen && status == CheckoutOrderStatus.PENDING) {
            this.validationHandler().append(new Error("'status' should be 'COMPLETED or ERROR' when is not open"));
        }

        if (isOpen && status == CheckoutOrderStatus.ERROR || isOpen && status == CheckoutOrderStatus.COMPLETED) {
            this.validationHandler().append(new Error("'status' should be 'PENDING' when is open"));
        }
    }
}
