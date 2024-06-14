package io.maju.frosty.validation.handler;

import io.maju.frosty.exceptions.DomainException;
import io.maju.frosty.validation.Error;
import io.maju.frosty.validation.ValidationHandler;

import java.util.List;

public class ThrowsValidationHandler implements ValidationHandler {

    @Override
    public ValidationHandler append(final Error anError) {
        throw DomainException.with(anError);
    }

    @Override
    public ValidationHandler append(ValidationHandler handler) {
        throw DomainException.with(handler.getErrors());
    }

    @Override
    public <T> T validate(Validation<T> t) {
        try {
            return t.validate();
        } catch (final Exception ex) {
            throw DomainException.with(new Error(ex.getMessage()));
        }
    }

    @Override
    public List<Error> getErrors() {
        return List.of();
    }
}
