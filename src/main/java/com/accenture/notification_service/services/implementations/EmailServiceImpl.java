package com.accenture.notification_service.services.implementations;

import com.accenture.notification_service.exceptions.EmailAuthenticationException;
import com.accenture.notification_service.exceptions.EmailException;
import com.accenture.notification_service.exceptions.EmailSendingException;
import com.accenture.notification_service.services.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;

@Slf4j
@RequiredArgsConstructor
@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Override
    public void sendAccreditationEmail(String email, byte[] pdfContent) {
        log.info("Starting processes for sending email accreditation");
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(email);
            helper.setSubject("Confirmation of your order");
            helper.setText("Attached you will find the details of your order. Thank you for choosing us! :D", true);
            helper.addAttachment("order.pdf", () -> new ByteArrayInputStream(pdfContent));

            mailSender.send(message);
            log.info("Email sent to {}", email);
        } catch (MailAuthenticationException ex) {
            log.error("Invalid credential for SMTP server");
            throw new EmailAuthenticationException("Invalid mail credentials", ex);
        } catch (MailSendException ex) {
            log.error("SMTP server not reachable");
            throw new EmailSendingException("SMTP server not reachable", ex);
        } catch (MessagingException ex) {
            log.error("Messaging error while sending email to {}", email);
            throw new EmailSendingException("Messaging error", ex);
        } catch (Exception ex) {
            log.error("Unexpected error while sending email");
            throw new EmailException("Unexpected error while sending email", ex);
        }

    }


}
