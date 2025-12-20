package com.postech.fiap.service;

import com.postech.fiap.model.EvaluationEntity;
import jakarta.enterprise.context.ApplicationScoped;

import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.List;

@ApplicationScoped
public class GenerateCsvReportService {

    public byte[] generateCSV(List<EvaluationEntity> evaluations) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        StringBuilder csvContent = new StringBuilder();

        csvContent.append("ID,Description,Rating,DateHourCreation\n");

        for (EvaluationEntity evaluation : evaluations) {
            csvContent.append(evaluation.id)
                    .append(",")
                    .append(escapeCSV(evaluation.getDescription()))
                    .append(",")
                    .append(evaluation.getRating())
                    .append(",")
                    .append(evaluation.getDateHourCriation().format(formatter))
                    .append("\n");
        }

        String csvString = String.join("\n", csvContent);
        return csvString.getBytes(StandardCharsets.UTF_8);
    }

    private String escapeCSV(String value) {
        if (value == null) {
            return "";
        }
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
}
