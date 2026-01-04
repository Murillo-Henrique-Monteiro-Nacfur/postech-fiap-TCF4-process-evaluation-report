package com.postech.fiap.usecase;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.postech.fiap.dto.AttachmentDto;
import com.postech.fiap.dto.EmailRequestDto;
import com.postech.fiap.gateway.UrgentWarningGateway;
import com.postech.fiap.model.AdministratorEntity;
import com.postech.fiap.model.EvaluationEntity;
import com.postech.fiap.service.GenerateCsvReportService;
import com.postech.fiap.service.GenerateEmailReportService;
import com.postech.fiap.service.GoogleStorageService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class GenerateReportUseCase {

    @Inject
    GoogleStorageService gcsUploadService;
    @Inject
    GenerateCsvReportService generateCsvReportService;
    @Inject
    GenerateEmailReportService generateEmailReportService;

    private final UrgentWarningGateway urgentWarningGateway;

    @Inject
    ObjectMapper objectMapper;

    public GenerateReportUseCase(UrgentWarningGateway urgentWarningGateway) {
        this.urgentWarningGateway = urgentWarningGateway;
    }

    @Transactional
    public void processAndSend() throws IOException {
        try {
            var emailRequestDto = generateEmailRequest();

            String jsonMessage = objectMapper.writeValueAsString(emailRequestDto);

            urgentWarningGateway.sendWarning(jsonMessage);
        } catch (Exception e) {
            throw new IOException("Falha ao enviar mensagem para o tópico", e);
        }
    }

    private List<EvaluationEntity> findEvaluationsCreatedInLastWeek() {
        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);

        return EvaluationEntity.find("dateHourCriation >= ?1", sevenDaysAgo).list();
    }

    private List<String> getEmailsToSendReport() {
        return AdministratorEntity.<AdministratorEntity>listAll().stream()
                .map(AdministratorEntity::getEmail)
                .toList();
    }

    private EmailRequestDto generateEmailRequest() throws IOException {
        var evaluationsInLastWeek = findEvaluationsCreatedInLastWeek();
        var generateCSV = generateCsvReportService.generateCSV(evaluationsInLastWeek);
        var emailMessage = generateEmailReportService.generateReport(evaluationsInLastWeek);
        var bucketName = "report-bucket-techfiap";
        var fileName = "Report-" + LocalDateTime.now() + ".csv";
        var objectName = gcsUploadService.uploadFile(bucketName, fileName, generateCSV, "report.csv");
        var emailsToSend = getEmailsToSendReport();


        AttachmentDto attachment = new AttachmentDto(bucketName, fileName);

        return new EmailRequestDto(
                emailsToSend,
                emailMessage,
                "Relatório Semanal de Avaliações - " + LocalDateTime.now().toLocalDate(),
                attachment
        );
    }
}
