package com.aplazo.bnpl.challenge.infrastructure.controller;

import com.aplazo.bnpl.challenge.application.service.RequestLogService;
import com.aplazo.bnpl.challenge.domain.model.RequestLog;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoggingControllerTest {

    @Mock
    private RequestLogService requestLogService;

    @InjectMocks
    private LoggingController loggingController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllLogs_ShouldReturnListOfLogs() {
        // GIVEN: A simulated list of logs returned by the service
        RequestLog log1 = new RequestLog();
        log1.setIp("192.168.1.1");
        log1.setHttpMethod("GET");
        log1.setMethod("getOrders");
        log1.setRequestDate(LocalDateTime.now());

        RequestLog log2 = new RequestLog();
        log2.setIp("10.0.0.1");
        log2.setHttpMethod("POST");
        log2.setMethod("createOrder");
        log2.setRequestDate(LocalDateTime.now());

        List<RequestLog> logs = Arrays.asList(log1, log2);
        when(requestLogService.getAllLogs()).thenReturn(logs);

        // WHEN: The `getAllLogs` endpoint is called
        ResponseEntity<List<RequestLog>> result = loggingController.getAllLogs();

        // THEN: It is validated that the response is as expected
        assertNotNull(result);
        assertEquals(200, result.getStatusCode().value());
        assertEquals(2, result.getBody().size());

        verify(requestLogService, times(1)).getAllLogs();
    }
}
