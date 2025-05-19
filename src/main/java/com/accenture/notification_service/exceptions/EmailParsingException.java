package com.accenture.notification_service.exceptions;

public class EmailParsingException extends EmailException {
    public EmailParsingException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
