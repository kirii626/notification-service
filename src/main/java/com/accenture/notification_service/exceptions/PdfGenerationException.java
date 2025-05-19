package com.accenture.notification_service.exceptions;

public class PdfGenerationException extends BusinessException {

  public PdfGenerationException(String message, Throwable throwable) {
    super(message, throwable);
  }
}
