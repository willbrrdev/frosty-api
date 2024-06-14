package io.maju.frosty.exceptions;

import io.maju.frosty.validation.handler.Notification;

import java.io.Serial;

public class NotificationException extends DomainException {

    @Serial
    private static final long serialVersionUID = -6197961396962192289L;

    public NotificationException(final String aMessage, final Notification notification) {
        super(aMessage, notification.getErrors());
    }
}
