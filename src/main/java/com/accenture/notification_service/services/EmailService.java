package com.accenture.notification_service.services;


public interface EmailService {

    void sendAccreditationEmail(String email, byte[] pdfContent);

}
