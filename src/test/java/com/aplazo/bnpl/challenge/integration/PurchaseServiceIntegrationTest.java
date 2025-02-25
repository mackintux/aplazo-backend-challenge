package com.aplazo.bnpl.challenge.integration;

import com.aplazo.bnpl.challenge.application.service.PurchaseServiceImpl;
import com.aplazo.bnpl.challenge.dto.PurchaseRequest;
import com.aplazo.bnpl.challenge.dto.PurchaseResponse;
import com.aplazo.bnpl.challenge.domain.model.Client;
import com.aplazo.bnpl.challenge.infrastructure.repository.ClientRepository;
import com.aplazo.bnpl.challenge.infrastructure.repository.PurchaseRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
class PurchaseServiceIntegrationTest {

    @Container
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("db_test")
            .withUsername("user")
            .withPassword("pass");

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private PurchaseServiceImpl purchaseService;

    @BeforeAll
    static void startContainer() {
        postgres.start();
        System.setProperty("spring.datasource.url", postgres.getJdbcUrl());
        System.setProperty("spring.datasource.username", postgres.getUsername());
        System.setProperty("spring.datasource.password", postgres.getPassword());
    }

    @BeforeEach
    void setUp() {
        Client client = new Client(1L, "John Doe", LocalDate.of(1995, 5, 10), 5000);
        clientRepository.save(client);
    }

    @Test
    void registerPurchase_ValidRequest_ShouldCreatePurchase() {
        PurchaseRequest request = new PurchaseRequest();
        request.setClientId(1L);
        request.setAmount(BigDecimal.valueOf(3000));

        PurchaseResponse response = purchaseService.registerPurchase(request);

        assertNotNull(response);
        assertEquals("Scheme 2", response.getPaymentScheme());
        assertEquals(BigDecimal.valueOf(3900), response.getTotalAmount());
    }
}