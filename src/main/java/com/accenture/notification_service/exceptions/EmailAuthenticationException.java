package com.accenture.notification_service.exceptions;

public class EmailAuthenticationException extends EmailException {
    public EmailAuthenticationException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
