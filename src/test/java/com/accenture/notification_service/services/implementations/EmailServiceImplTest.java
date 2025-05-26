package com.accenture.notification_service.services.implementations;

import com.accenture.notification_service.exceptions.EmailAuthenticationException;
import com.accenture.notification_service.exceptions.EmailException;
import com.accenture.notification_service.exceptions.EmailSendingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceImplTest {

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private MimeMessage mimeMessage;

    @InjectMocks
    private EmailServiceImpl emailService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        JavaMailSenderImpl realMailSender = new JavaMailSenderImpl();
        realMailSender.setHost("localhost");
        realMailSender.setPort(3025);


        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        emailService = new EmailServiceImpl(mailSender);
    }


    @Test
    void sendAccreditationEmail_shouldThrowEmailAuthenticationException_whenAuthFails() {
        when(mailSender.createMimeMessage()).thenThrow(new MailAuthenticationException("Invalid"));

        assertThrows(EmailAuthenticationException.class,
                () -> emailService.sendAccreditationEmail("test@example.com", new byte[0]));
    }


    @Test
    void sendAccreditationEmail_shouldThrowAuthenticationException() {
        byte[] pdfBytes = "PDF content".getBytes();
        String email = "test@example.com";

        when(mailSender.createMimeMessage()).thenThrow(new MailAuthenticationException("Auth failed"));

        EmailAuthenticationException ex = assertThrows(EmailAuthenticationException.class, () -> {
            emailService.sendAccreditationEmail(email, pdfBytes);
        });

        assertTrue(ex.getMessage().contains("Invalid mail credentials"));
    }

    @Test
    void sendAccreditationEmail_shouldThrowSendingExceptionOnMailSend() throws Exception {
        byte[] pdfBytes = "PDF content".getBytes();
        String email = "test@example.com";

        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        doThrow(new MailSendException("SMTP down")).when(mailSender).send(mimeMessage);

        EmailSendingException ex = assertThrows(EmailSendingException.class, () -> {
            emailService.sendAccreditationEmail(email, pdfBytes);
        });

        assertTrue(ex.getMessage().contains("SMTP server not reachable"));
    }

    @Test
    void sendAccreditationEmail_shouldThrowGenericException() {
        byte[] pdfBytes = "PDF content".getBytes();
        String email = "test@example.com";

        when(mailSender.createMimeMessage()).thenThrow(new RuntimeException("Unexpected"));

        EmailException ex = assertThrows(EmailException.class, () -> {
            emailService.sendAccreditationEmail(email, pdfBytes);
        });

        assertTrue(ex.getMessage().contains("Unexpected error while sending email"));
    }
}
