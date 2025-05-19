package com.accenture.notification_service.services.implementations;

import com.accenture.notification_service.client.dtos.AccreditationDtoOutput;
import com.accenture.notification_service.exceptions.PdfGenerationException;
import com.accenture.notification_service.services.PdfGeneratorService;
import com.itextpdf.io.exceptions.IOException;
import com.itextpdf.kernel.exceptions.PdfException;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;

import static com.itextpdf.io.font.constants.StandardFontFamilies.HELVETICA;
import static com.itextpdf.io.font.constants.StandardFonts.HELVETICA_BOLD;

@Service
@Slf4j
public class PdfGeneratorServiceImpl implements PdfGeneratorService {

    @Override
    public byte[] generateAccreditationPdf(AccreditationDtoOutput accreditationDtoOutput) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            PdfWriter writer = new PdfWriter(outputStream);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            PdfFont boldFont = PdfFontFactory.createFont(HELVETICA_BOLD);
            PdfFont normalFont = PdfFontFactory.createFont(HELVETICA);

            DecimalFormat currencyFormat = new DecimalFormat("$#,##0.00");

            Paragraph title = new Paragraph("Accreditation Receipt #" + accreditationDtoOutput.getAccreditationId())
                    .setFont(boldFont)
                    .setFontSize(18)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(20);
            document.add(title);

            Table table = new Table(UnitValue.createPercentArray(new float[]{1, 2}))
                    .useAllAvailableWidth();

            table.addCell(new Cell().add(new Paragraph("Sale Point").setFont(boldFont)));
            table.addCell(new Cell().add(new Paragraph(accreditationDtoOutput.getNameSalePoint()).setFont(normalFont)));

            table.addCell(new Cell().add(new Paragraph("Sale Point ID").setFont(boldFont)));
            table.addCell(new Cell().add(new Paragraph(accreditationDtoOutput.getSalePointId().toString()).setFont(normalFont)));

            table.addCell(new Cell().add(new Paragraph("User").setFont(boldFont)));
            table.addCell(new Cell().add(new Paragraph(accreditationDtoOutput.getUsername()).setFont(normalFont)));

            table.addCell(new Cell().add(new Paragraph("User Email").setFont(boldFont)));
            table.addCell(new Cell().add(new Paragraph(accreditationDtoOutput.getEmail()).setFont(normalFont)));

            table.addCell(new Cell().add(new Paragraph("Amount").setFont(boldFont)));
            table.addCell(new Cell().add(new Paragraph(currencyFormat.format(accreditationDtoOutput.getAmount())).setFont(normalFont)));

            table.addCell(new Cell().add(new Paragraph("Received At").setFont(boldFont)));
            table.addCell(new Cell().add(new Paragraph(accreditationDtoOutput.getReceivedAt().toString()).setFont(normalFont)));

            document.add(table);

            Paragraph footer = new Paragraph("Thank you for your accreditation.")
                    .setFont(normalFont)
                    .setFontSize(12)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginTop(30);
            document.add(footer);

            document.close();

            return outputStream.toByteArray();
        } catch (IOException | PdfException ex) {
            log.error("PDF generation error for Accreditation ID: {}", accreditationDtoOutput.getAccreditationId(), ex);
            throw new PdfGenerationException("Failed to generate PDF", ex);
        } catch (Exception ex) {
            log.error("Unexpected error during PDF generation", ex);
            throw new PdfGenerationException("Unexpected PDF generation error", ex);
        }
    }
}
