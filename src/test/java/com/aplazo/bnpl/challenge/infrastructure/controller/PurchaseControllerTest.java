package com.aplazo.bnpl.challenge.infrastructure.controller;

import com.aplazo.bnpl.challenge.application.service.PurchaseService;
import com.aplazo.bnpl.challenge.dto.PurchaseRequest;
import com.aplazo.bnpl.challenge.dto.PurchaseResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PurchaseControllerTest {

    @Mock
    private PurchaseService purchaseService;

    @InjectMocks
    private PurchaseController purchaseController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerPurchase_ShouldReturnPurchaseResponse() {
        // GIVEN: A `PurchaseRequest` and an expected response from the service
        PurchaseRequest request = new PurchaseRequest();

        List<LocalDate> paymentDates = Arrays.asList(
                LocalDate.of(2025, 3, 1),
                LocalDate.of(2025, 3, 15),
                LocalDate.of(2025, 3, 30)
        );

        PurchaseResponse response = new PurchaseResponse(
                1L,
                "Scheme 1",
                new BigDecimal("5000.00"),
                new BigDecimal("500.00"),
                new BigDecimal("1833.33"),
                paymentDates
        );

        when(purchaseService.registerPurchase(any(PurchaseRequest.class))).thenReturn(response);

        // WHEN: The `registerPurchase` endpoint is called
        ResponseEntity<PurchaseResponse> result = purchaseController.registerPurchase(request);

        // THEN: It is validated that the response is the expected one
        assertNotNull(result);
        assertEquals(200, result.getStatusCode().value());
        assertNotNull(result.getBody());

        PurchaseResponse responseBody = result.getBody();
        assertEquals(1L, responseBody.getId());
        assertEquals("Scheme 1", responseBody.getPaymentScheme());
        assertEquals(new BigDecimal("5000.00"), responseBody.getTotalAmount());
        assertEquals(new BigDecimal("500.00"), responseBody.getCommissionAmount());
        assertEquals(new BigDecimal("1833.33"), responseBody.getInstallmentAmount());
        assertEquals(paymentDates, responseBody.getPaymentDueDates());

        verify(purchaseService, times(1)).registerPurchase(request);
    }
}
