package io.maju.frosty.validation;

import java.util.List;

public interface ValidationHandler {

    ValidationHandler append(final Error anError);

    ValidationHandler append(final ValidationHandler handler);

    <T> T validate(final Validation<T> t);

    default boolean hasError() {
        return getErrors() != null && !getErrors().isEmpty();
    }

    default Error firstError() {
        if (getErrors() != null && !getErrors().isEmpty()) {
            return getErrors().stream().findFirst().orElse(null);
        } else {
            return null;
        }
    }

    List<Error> getErrors();

    interface Validation<T> {
        T validate();
    }
}
