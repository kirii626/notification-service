package com.accenture.notification_service.events.listeners;

import com.accenture.notification_service.client.dtos.AccreditationDtoOutput;
import com.accenture.notification_service.exceptions.EmailException;
import com.accenture.notification_service.services.EmailService;
import com.accenture.notification_service.services.PdfGeneratorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
@Slf4j
public class AccreditationCreatedListener {
    
    private final EmailService emailService;
    private final PdfGeneratorService pdfGeneratorService;

    @RabbitListener(queues = "${rabbitmq.queue.accreditation}")
    public void handleOrderCreatedEvent(AccreditationDtoOutput accreditationDtoOutput) {
        log.info("Order received: {}", accreditationDtoOutput.getAccreditationId());
        log.info("Searching email's user with ID: {}", accreditationDtoOutput.getUserId());

        String userEmail = accreditationDtoOutput.getEmail();

        if (userEmail == null || userEmail.isBlank()) {
            log.error("User email is missing for accreditation ID: {}", accreditationDtoOutput.getAccreditationId());
            throw new EmailException("Missing email for user ID: " + accreditationDtoOutput.getUserId());
        }

        byte[] pdfContent = pdfGeneratorService.generateAccreditationPdf(accreditationDtoOutput);
        emailService.sendAccreditationEmail(userEmail, pdfContent);
    }
}
