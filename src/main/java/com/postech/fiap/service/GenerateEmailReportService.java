package com.postech.fiap.service;

import com.postech.fiap.model.EvaluationEntity;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ApplicationScoped
public class GenerateEmailReportService {

    public String generateReport(List<EvaluationEntity> evaluations) {
        Integer medEvaluations = evaluations.stream().mapToInt(EvaluationEntity::getRating).sum() / evaluations.size();

        Map<LocalDate, Long> evaluationsPerDay = evaluations.stream()
                .collect(Collectors.groupingBy(evaluation -> evaluation.getDateHourCriation().toLocalDate(), Collectors.counting()));

        Map<String, Long> evaluationsByUrgency = evaluations.stream()
                .collect(Collectors.groupingBy(this::getUrgency, Collectors.counting()));

        StringBuilder report = new StringBuilder();
        report.append("Relatório de Avaliações:\n\n");
        report.append("Quantidade de avaliações por dia:\n");
        evaluationsPerDay.forEach((date, count) -> report.append("- ").append(date).append(": ").append(count).append("\n"));

        report.append("\nQuantidade de avaliações por urgência:\n");
        evaluationsByUrgency.forEach((urgency, count) -> report.append("- ").append(urgency).append(": ").append(count).append("\n"));
        report.append("\nMédia de avaliações: ").append(medEvaluations);

        return report.toString();
    }

    private String getUrgency(EvaluationEntity evaluation) {
        int rating = evaluation.getRating();
        if (rating >= 0 && rating <= 5) {
            return "Urgente";
        } else if (rating >= 6 && rating <= 8) {
            return "Normal";
        } else {
            return "Bom";
        }
    }
}
