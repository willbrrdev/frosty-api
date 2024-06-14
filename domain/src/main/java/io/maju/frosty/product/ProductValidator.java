package io.maju.frosty.product;

import io.maju.frosty.validation.Error;
import io.maju.frosty.validation.ValidationHandler;
import io.maju.frosty.validation.Validator;

import java.math.BigDecimal;

public class ProductValidator extends Validator {

    public static final int NAME_MIN_LENGTH = 3;
    public static final int NAME_MAX_LENGTH = 255;

    private final Product product;

    public ProductValidator(final Product aProduct, final ValidationHandler aHandler) {
        super(aHandler);
        this.product = aProduct;
    }

    @Override
    public void validate() {
        checkNameConstraints();
        checkPriceConstraints();
    }

    private void checkNameConstraints() {
        final var name = this.product.name();
        if (name == null) {
            this.validationHandler().append(new Error("'name' should not be null"));
            return;
        }

        if (name.isBlank()) {
            this.validationHandler().append(new Error("'name' should not be empty"));
            return;
        }

        final int length = name.trim().length();
        if (length > NAME_MAX_LENGTH || length < NAME_MIN_LENGTH) {
            this.validationHandler().append(new Error("'name' must be between 3 and 255 characters"));
        }
    }

    private void checkPriceConstraints() {
        final var price = this.product.price();
        if (price == null) {
            this.validationHandler().append(new Error("'price' should not be null"));
            return;
        }

        if (price.compareTo(BigDecimal.ZERO) <= 0) {
            this.validationHandler().append(new Error("'price' should be greater than zero"));
        }
    }
}
