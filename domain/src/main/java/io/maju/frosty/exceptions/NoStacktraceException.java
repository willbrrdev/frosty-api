package io.maju.frosty.exceptions;

import java.io.Serial;

public class NoStacktraceException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -1655132756095691921L;

    public NoStacktraceException(final String message) {
        this(message, null);
    }

    public NoStacktraceException(final String message, final Throwable cause) {
        super(message, cause, true, false);
    }

}
