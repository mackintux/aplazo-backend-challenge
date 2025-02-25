package com.aplazo.bnpl.challenge.application.service;

import com.aplazo.bnpl.challenge.dto.PurchaseRequest;
import com.aplazo.bnpl.challenge.dto.PurchaseResponse;
import com.aplazo.bnpl.challenge.domain.model.Client;
import com.aplazo.bnpl.challenge.domain.model.Purchase;
import com.aplazo.bnpl.challenge.infrastructure.repository.ClientRepository;
import com.aplazo.bnpl.challenge.infrastructure.repository.PurchaseRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Assertions;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.Mockito.*;

class PurchaseServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private PurchaseRepository purchaseRepository;

    @InjectMocks
    private PurchaseServiceImpl purchaseService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerPurchase_ValidRequest_ShouldCreatePurchaseWithCorrectCalculations() {
        // GIVEN
        Client client = new Client(1L, "Carlos", LocalDate.of(1995, 5, 10), 5000);
        PurchaseRequest request = new PurchaseRequest();
        request.setClientId(1L);
        request.setAmount(BigDecimal.valueOf(3000));

        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(purchaseRepository.save(any(Purchase.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // WHEN
        PurchaseResponse response = purchaseService.registerPurchase(request);

        // THEN: Validate the payment scheme assignment
        Assertions.assertNotNull(response);
        Assertions.assertEquals("Scheme 1", response.getPaymentScheme()); // "Carlos" starts with "C", it must be Scheme 1

        // Validate commission calculation (13% interest on Scheme 1)
        BigDecimal expectedCommission = BigDecimal.valueOf(3000).multiply(BigDecimal.valueOf(0.13));
        Assertions.assertEquals(expectedCommission, response.getCommissionAmount());

        // Validate calculation of the total amount
        BigDecimal expectedTotalAmount = BigDecimal.valueOf(3000).add(expectedCommission);
        Assertions.assertEquals(expectedTotalAmount, response.getTotalAmount());

        // Validate calculation of the amount per installment
        BigDecimal expectedInstallmentAmount = expectedTotalAmount.divide(BigDecimal.valueOf(5), 2, BigDecimal.ROUND_HALF_UP);
        Assertions.assertEquals(expectedInstallmentAmount, response.getInstallmentAmount());

        // Validate payment dates
        Assertions.assertEquals(5, response.getPaymentDueDates().size());
        Assertions.assertTrue(response.getPaymentDueDates().get(0).isAfter(LocalDate.now()));
    }

    @Test
    void registerPurchase_ExceedsCredit_ShouldThrowException() {
        // GIVEN
        Client client = new Client(1L, "John Doe", LocalDate.of(1995, 5, 10), 5000);
        PurchaseRequest request = new PurchaseRequest();
        request.setClientId(1L);
        request.setAmount(BigDecimal.valueOf(6000)); // Exceeds the credit line

        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));

        // WHEN & THEN: We expect an exception
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            purchaseService.registerPurchase(request);
        });
    }

    @Test
    void registerPurchase_ShouldAssignScheme2_WhenClientNameDoesNotStartWith_C_L_H() {
        // GIVEN
        Client client = new Client(1L, "Mario", LocalDate.of(1995, 5, 10), 5000);
        PurchaseRequest request = new PurchaseRequest();
        request.setClientId(1L);
        request.setAmount(BigDecimal.valueOf(2500));

        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(purchaseRepository.save(any(Purchase.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // WHEN
        PurchaseResponse response = purchaseService.registerPurchase(request);

        // THEN
        Assertions.assertEquals("Scheme 2", response.getPaymentScheme()); // "Mario" does not start with "C, L, H", assigns Scheme 2
    }
}
