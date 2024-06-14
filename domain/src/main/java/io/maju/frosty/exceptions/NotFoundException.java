package io.maju.frosty.exceptions;

import io.maju.frosty.AggregateRoot;
import io.maju.frosty.Identifier;
import io.maju.frosty.validation.Error;

import java.io.Serial;
import java.util.Collections;
import java.util.List;

public class NotFoundException extends DomainException {

    @Serial
    private static final long serialVersionUID = 5357904655371061528L;

    protected NotFoundException(final String aMessage, final List<Error> anErrors) {
        super(aMessage, anErrors);
    }

    public static NotFoundException with(
            final Class<? extends AggregateRoot<?>> anAggregate,
            final Identifier id
    ) {
        final var anError = "%s with ID %s was not found".formatted(
                anAggregate.getSimpleName(),
                id.getValue()
        );

        return new NotFoundException(anError, Collections.emptyList());
    }
}
