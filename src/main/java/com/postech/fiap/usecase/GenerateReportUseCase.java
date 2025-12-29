package com.postech.fiap.usecase;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.PubsubMessage;
import com.postech.fiap.dto.AttachmentDto;
import com.postech.fiap.dto.EmailRequestDto;
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
    @Inject
    Publisher userEventsPublisher;

    @Inject
    ObjectMapper objectMapper;

    @Transactional
    public void processAndSend() throws IOException {
        try {
            var emailRequestDto = generateEmailRequest();

            String jsonMessage = objectMapper.writeValueAsString(emailRequestDto);

            ByteString data = ByteString.copyFromUtf8(jsonMessage);

            PubsubMessage pubsubMessage = PubsubMessage.newBuilder()
                    .setData(data)
                    .build();

            userEventsPublisher.publish(pubsubMessage).get();
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
        var fileName = "Report-" + LocalDateTime.now();
        var objectName = gcsUploadService.uploadFile(bucketName, fileName, generateCSV, "report.csv");
        var emailsToSend = getEmailsToSendReport();

        AttachmentDto attachment = new AttachmentDto(bucketName, fileName + ".csv");

        return new EmailRequestDto(
                emailsToSend,
                emailMessage,
                "Relatório Semanal de Avaliações - " + LocalDateTime.now().toLocalDate(),
                attachment
        );
    }
}
