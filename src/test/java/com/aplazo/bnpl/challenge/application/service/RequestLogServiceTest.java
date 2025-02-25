package com.aplazo.bnpl.challenge.application.service;

import com.aplazo.bnpl.challenge.domain.model.RequestLog;
import com.aplazo.bnpl.challenge.infrastructure.repository.RequestLogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RequestLogServiceTest {

    @Mock
    private RequestLogRepository requestLogRepository;

    @InjectMocks
    private RequestLogServiceImpl requestLogService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void logRequest_ShouldSaveRequestLog() {
        // GIVEN
        String ip = "192.168.1.1";
        String httpMethod = "POST";
        String method = "createOrder";
        String requestPayload = "{\"order\": \"123\"}";
        String responsePayload = "{\"status\": \"success\"}";
        long duration = 250;

        // WHEN
        requestLogService.logRequest(ip, httpMethod, method, requestPayload, responsePayload, duration);

        // THEN: Capture the saved entity
        ArgumentCaptor<RequestLog> logCaptor = ArgumentCaptor.forClass(RequestLog.class);
        verify(requestLogRepository, times(1)).save(logCaptor.capture());

        RequestLog savedLog = logCaptor.getValue();
        assertNotNull(savedLog);
        assertEquals(ip, savedLog.getIp());
        assertEquals(httpMethod, savedLog.getHttpMethod());
        assertEquals(method, savedLog.getMethod());
        assertEquals(requestPayload, savedLog.getRequestPayload());
        assertEquals(responsePayload, savedLog.getResponsePayload());
        assertEquals(duration, savedLog.getDuration());
        assertNotNull(savedLog.getRequestDate());
    }

    @Test
    void logRequest_ShouldUseDefaultIp_WhenIpIsNull() {
        // GIVEN
        String httpMethod = "POST";
        String method = "createOrder";
        String requestPayload = "{\"order\": \"123\"}";
        String responsePayload = "{\"status\": \"success\"}";
        long duration = 250;

        // WHEN
        requestLogService.logRequest(null, httpMethod, method, requestPayload, responsePayload, duration);

        // THEN: Capture the saved entity
        ArgumentCaptor<RequestLog> logCaptor = ArgumentCaptor.forClass(RequestLog.class);
        verify(requestLogRepository, times(1)).save(logCaptor.capture());

        RequestLog savedLog = logCaptor.getValue();
        assertEquals("127.0.0.1", savedLog.getIp()); // Default IP should be set
    }

    @Test
    void getAllLogs_ShouldReturnListOfRequestLogs() {
        // GIVEN
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

        when(requestLogRepository.findAll()).thenReturn(Arrays.asList(log1, log2));

        // WHEN
        List<RequestLog> logs = requestLogService.getAllLogs();

        // THEN
        assertNotNull(logs);
        assertEquals(2, logs.size());
        assertEquals("192.168.1.1", logs.get(0).getIp());
        assertEquals("GET", logs.get(0).getHttpMethod());
        assertEquals("10.0.0.1", logs.get(1).getIp());
        assertEquals("POST", logs.get(1).getHttpMethod());

        verify(requestLogRepository, times(1)).findAll();
    }
}
