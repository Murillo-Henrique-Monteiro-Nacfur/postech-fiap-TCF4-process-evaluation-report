package com.postech.fiap;

import com.postech.fiap.usecase.GenerateReportUseCase;
import io.quarkus.funqy.Funq;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;

@ApplicationScoped
public class ProcessEvaluationReportFunction {

    @Inject
    GenerateReportUseCase generateReportUseCase;

    private static final Logger LOGGER = Logger.getLogger(ProcessEvaluationReportFunction.class.getName());

    public static class PubSubMessage {
        public Message message;
        public String subscription;
    }

    public static class Message {
        public String data;
        public Map<String, String> attributes;
        public String messageId;
    }

    @Funq("scheduledReport")
    public void scheduledReport(PubSubMessage message) throws IOException {
        LOGGER.info("Scheduler trigger received. Message ID: " + message.message.data);
        generateReportUseCase.processAndSend();
        LOGGER.info("Scheduled task finished.");
    }
}
