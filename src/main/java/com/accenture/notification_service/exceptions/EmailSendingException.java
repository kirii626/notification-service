package com.accenture.notification_service.exceptions;

public class EmailSendingException extends EmailException {

    public EmailSendingException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
