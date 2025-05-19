package com.accenture.notification_service.services;

import com.accenture.notification_service.client.dtos.AccreditationDtoOutput;


public interface PdfGeneratorService {

    byte[] generateAccreditationPdf(AccreditationDtoOutput accreditationDtoOutput);
}
