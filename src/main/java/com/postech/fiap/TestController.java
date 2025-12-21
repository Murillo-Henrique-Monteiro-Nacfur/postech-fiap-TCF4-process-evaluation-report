package com.postech.fiap;

import com.postech.fiap.usecase.GenerateReportUseCase;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.logging.Logger;

@Path("/test")
public class TestController {

    private static final Logger LOGGER = Logger.getLogger(TestController.class.getName());

    @Inject
    GenerateReportUseCase generateReportUseCase;

    @GET
    @Path("/trigger-report")
    @Produces(MediaType.TEXT_PLAIN)
    public String triggerReport() {
        LOGGER.info("Manual trigger received via HTTP");
        try {
            generateReportUseCase.processAndSend();
            LOGGER.info("Report generation completed successfully");
            return "Report generation triggered successfully!";
        } catch (Exception e) {
            LOGGER.severe("Error generating report: " + e.getMessage());
            return "Error: " + e.getMessage();
        }
    }
}

