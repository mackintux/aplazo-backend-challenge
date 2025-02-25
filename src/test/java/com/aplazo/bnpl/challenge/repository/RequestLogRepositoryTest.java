package com.aplazo.bnpl.challenge.repository;

import com.aplazo.bnpl.challenge.domain.model.RequestLog;
import com.aplazo.bnpl.challenge.infrastructure.repository.RequestLogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class RequestLogRepositoryTest {

    @Autowired
    private RequestLogRepository requestLogRepository;

    @BeforeEach
    void setUp() {
        // GIVEN: Logs are saved in the database
        RequestLog log1 = RequestLog.builder()
                .ip("192.168.1.1")
                .requestDate(LocalDateTime.now())
                .method("registerClient")
                .httpMethod("POST")
                .duration(120L)
                .requestPayload("{\"name\":\"John\"}")
                .responsePayload("{\"id\":1,\"creditLine\":5000}")
                .build();

        RequestLog log2 = RequestLog.builder()
                .ip("10.0.0.1")
                .requestDate(LocalDateTime.now())
                .method("getClient")
                .httpMethod("GET")
                .duration(50L)
                .requestPayload("{}")
                .responsePayload("{\"id\":1,\"creditLine\":5000}")
                .build();

        requestLogRepository.saveAll(List.of(log1, log2));
    }

    @Test
    void save_ShouldPersistLog() {
        // GIVEN: A new log
        RequestLog log = RequestLog.builder()
                .ip("127.0.0.1")
                .requestDate(LocalDateTime.now())
                .method("deleteClient")
                .httpMethod("DELETE")
                .duration(200L)
                .requestPayload("{\"id\":1}")
                .responsePayload("{\"status\":\"deleted\"}")
                .build();

        // WHEN: We keep the log
        RequestLog savedLog = requestLogRepository.save(log);

        // THEN: Must be persisted correctly
        assertNotNull(savedLog.getId());
        assertEquals("DELETE", savedLog.getHttpMethod());
        assertEquals("deleteClient", savedLog.getMethod());
        assertEquals("{\"status\":\"deleted\"}", savedLog.getResponsePayload());
    }

    @Test
    void findById_ShouldReturnRequestLog() {
        // WHEN: We search for a log by ID
        Optional<RequestLog> result = requestLogRepository.findById(1L);

        // THEN: The log must be found
        assertTrue(result.isPresent());
        assertEquals("POST", result.get().getHttpMethod());
        assertEquals("registerClient", result.get().getMethod());
    }

    @Test
    void findById_ShouldReturnEmpty_WhenLogNotExists() {
        // WHEN: We are looking for a non-existent ID
        Optional<RequestLog> result = requestLogRepository.findById(999L);

        // THEN: Should not be found
        assertFalse(result.isPresent());
    }

    @Test
    void findAll_ShouldReturnAllLogs() {
        // WHEN: We get all the logs
        List<RequestLog> logs = requestLogRepository.findAll();

        // THEN: Logs saved in `setUp()` should be obtained
        assertEquals(2, logs.size());
    }
}
