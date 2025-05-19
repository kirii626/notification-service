package com.accenture.notification_service.exceptions;

import lombok.Getter;

@Getter
public class EmailException extends BusinessException {

    public EmailException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public EmailException(String message) {
        super(message);
    }
}
