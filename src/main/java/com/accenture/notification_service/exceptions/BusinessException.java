package com.accenture.notification_service.exceptions;

public abstract class BusinessException extends RuntimeException {
    protected BusinessException(String message) {
        super(message);
    }

    protected BusinessException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
