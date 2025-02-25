package com.aplazo.bnpl.challenge.infrastructure.controller;

import com.aplazo.bnpl.challenge.application.service.RequestLogService;
import com.aplazo.bnpl.challenge.domain.model.RequestLog;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller for managing and retrieving request logs.
 * This controller provides an endpoint to fetch all request logs recorded by the application.
 */
@Tag(
        name = "Logging",
        description = "API for managing and retrieving request logs."
)
@RestController
@RequestMapping("/logs") // Base URL for all log-related endpoints
public class LoggingController {

    private final RequestLogService requestLogService;

    /**
     * Constructor for injecting dependencies.
     *
     * @param requestLogService the service responsible for managing request logs
     */
    public LoggingController(RequestLogService requestLogService) {
        this.requestLogService = requestLogService;
    }

    /**
     * Retrieves all logged requests.
     *
     * @return a {@link ResponseEntity} containing a list of {@link RequestLog} objects
     */
    @Operation(
            summary = "Retrieve all request logs",
            description = "Returns a list of all logged requests."
    )
    @GetMapping // HTTP GET method
    public ResponseEntity<List<RequestLog>> getAllLogs() {
        // Fetch all logs using the RequestLogService
        List<RequestLog> logs = requestLogService.getAllLogs();

        // Return the logs as an HTTP response with status 200 (OK)
        return ResponseEntity.ok(logs);
    }

}
