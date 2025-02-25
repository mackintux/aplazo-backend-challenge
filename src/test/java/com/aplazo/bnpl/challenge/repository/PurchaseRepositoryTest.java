package com.aplazo.bnpl.challenge.repository;

import com.aplazo.bnpl.challenge.domain.model.Client;
import com.aplazo.bnpl.challenge.domain.model.Purchase;
import com.aplazo.bnpl.challenge.infrastructure.repository.ClientRepository;
import com.aplazo.bnpl.challenge.infrastructure.repository.PurchaseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class PurchaseRepositoryTest {

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private ClientRepository clientRepository;

    private Client client;

    @BeforeEach
    void setUp() {
        // Create and save a client
        client = Client.builder()
                .name("John Doe")
                .birthDate(LocalDate.of(1995, 5, 10))
                .creditLine(5000)
                .build();
        client = clientRepository.save(client);

        // Create and save a purchase
        Purchase purchase = Purchase.builder()
                .client(client)
                .amount(new BigDecimal("3000"))
                .paymentScheme("Scheme 1")
                .numberOfPayments(5)
                .interestRate(new BigDecimal("13"))
                .totalAmount(new BigDecimal("3900"))
                .commissionAmount(new BigDecimal("900"))
                .installmentAmount(new BigDecimal("780"))
                .purchaseDate(LocalDate.now())
                .paymentDueDates(List.of(
                        LocalDate.now().plusDays(15),
                        LocalDate.now().plusDays(30),
                        LocalDate.now().plusDays(45)
                ))
                .build();
        purchaseRepository.save(purchase);
    }

    @Test
    void save_ShouldPersistPurchase() {
        // GIVEN: A new purchase
        Purchase purchase = Purchase.builder()
                .client(client)
                .amount(new BigDecimal("2000"))
                .paymentScheme("Scheme 2")
                .numberOfPayments(3)
                .interestRate(new BigDecimal("10"))
                .totalAmount(new BigDecimal("2200"))
                .commissionAmount(new BigDecimal("200"))
                .installmentAmount(new BigDecimal("733.33"))
                .purchaseDate(LocalDate.now())
                .paymentDueDates(List.of(
                        LocalDate.now().plusDays(10),
                        LocalDate.now().plusDays(20),
                        LocalDate.now().plusDays(30)
                ))
                .build();

        // WHEN: We keep the purchase
        Purchase savedPurchase = purchaseRepository.save(purchase);

        // THEN: Must be persisted correctly
        assertNotNull(savedPurchase.getId());
        assertEquals(client.getId(), savedPurchase.getClient().getId());
        assertEquals("Scheme 2", savedPurchase.getPaymentScheme());
        assertEquals(new BigDecimal("2200"), savedPurchase.getTotalAmount());
    }

    @Test
    void findById_ShouldReturnPurchase() {
        // WHEN: We are looking for a purchase by ID
        Optional<Purchase> result = purchaseRepository.findById(1L);

        // THEN: Purchase must be found
        assertTrue(result.isPresent());
        assertEquals("Scheme 1", result.get().getPaymentScheme());
    }

    @Test
    void findById_ShouldReturnEmpty_WhenPurchaseNotExists() {
        // WHEN: We are looking for a non-existent ID
        Optional<Purchase> result = purchaseRepository.findById(999L);

        // THEN: Should not be found
        assertFalse(result.isPresent());
    }
}
