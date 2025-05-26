package com.accenture.notification_service.services.implementations;

import com.accenture.notification_service.client.dtos.AccreditationDtoOutput;
import com.accenture.notification_service.exceptions.PdfGenerationException;
import com.itextpdf.io.exceptions.IOException;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFontFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PdfGeneratorServiceImplTest {

    private PdfGeneratorServiceImpl pdfGeneratorService;

    private AccreditationDtoOutput mockDto;

    @BeforeEach
    void setUp() {
        pdfGeneratorService = new PdfGeneratorServiceImpl();

        mockDto = new AccreditationDtoOutput(
                1L,
                5000F,
                LocalDateTime.now(),
                101L,
                "TestSalePoint",
                1500L,
                "username",
                "test@example.com"
        );
    }

    @Test
    void generateAccreditationPdf_shouldReturnNonEmptyByteArray() {
        byte[] pdfBytes = pdfGeneratorService.generateAccreditationPdf(mockDto);
        assertNotNull(pdfBytes);
        assertTrue(pdfBytes.length > 0);
    }

    @Test
    void generateAccreditationPdf_shouldThrowPdfGenerationException_whenFontCreationFails() {
        try (MockedStatic<PdfFontFactory> mockedStatic = Mockito.mockStatic(PdfFontFactory.class)) {
            mockedStatic.when(() -> PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD))
                    .thenThrow(new IOException("Font error"));

            PdfGenerationException ex = assertThrows(
                    PdfGenerationException.class,
                    () -> pdfGeneratorService.generateAccreditationPdf(mockDto)
            );

            assertTrue(ex.getMessage().contains("Failed to generate PDF"));
        }
    }

    @Test
    void generateAccreditationPdf_shouldThrowPdfGenerationException_whenUnexpectedExceptionOccurs() {
        AccreditationDtoOutput badDto = mock(AccreditationDtoOutput.class);
        when(badDto.getAccreditationId()).thenThrow(new RuntimeException("Unexpected"));

        PdfGenerationException ex = assertThrows(
                PdfGenerationException.class,
                () -> pdfGeneratorService.generateAccreditationPdf(badDto)
        );

        assertTrue(ex.getMessage().contains("Unexpected PDF generation error"));
    }
}
