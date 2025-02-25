package com.aplazo.bnpl.challenge.application.service;

import com.aplazo.bnpl.challenge.dto.PurchaseRequest;
import com.aplazo.bnpl.challenge.dto.PurchaseResponse;
import com.aplazo.bnpl.challenge.domain.model.Client;
import com.aplazo.bnpl.challenge.domain.model.Purchase;
import com.aplazo.bnpl.challenge.infrastructure.repository.ClientRepository;
import com.aplazo.bnpl.challenge.infrastructure.repository.PurchaseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class PurchaseServiceImpl implements PurchaseService {

    private final ClientRepository clientRepository;
    private final PurchaseRepository purchaseRepository;

    public PurchaseServiceImpl(ClientRepository clientRepository, PurchaseRepository purchaseRepository) {
        this.clientRepository = clientRepository;
        this.purchaseRepository = purchaseRepository;
    }

    @Override
    @Transactional
    public PurchaseResponse registerPurchase(PurchaseRequest request) {
        Client client = clientRepository.findById(request.getClientId())
                .orElseThrow(() -> new IllegalArgumentException("Client not found"));

        if (request.getAmount().compareTo(BigDecimal.valueOf(client.getCreditLine())) > 0) {
            throw new IllegalArgumentException("Purchase amount exceeds available credit line");
        }

        // Allocation of payment scheme
        String paymentScheme = assignPaymentScheme(client.getName(), client.getId());

        // Determination of interest based on the scheme
        BigDecimal interestRate = paymentScheme.equals("Scheme 1") ? BigDecimal.valueOf(0.13) : BigDecimal.valueOf(0.16);
        BigDecimal commissionAmount = request.getAmount().multiply(interestRate);
        BigDecimal totalAmount = request.getAmount().add(commissionAmount);
        BigDecimal installmentAmount = totalAmount.divide(BigDecimal.valueOf(5), 2, BigDecimal.ROUND_HALF_UP);

        // Generation of payment dates
        List<LocalDate> paymentDueDates = generatePaymentDueDates(LocalDate.now(), 5, "Biweekly");

        Purchase purchase = new Purchase();
        purchase.setClient(client);
        purchase.setAmount(request.getAmount());
        purchase.setPaymentScheme(paymentScheme);
        purchase.setNumberOfPayments(5);
        purchase.setInterestRate(interestRate);
        purchase.setCommissionAmount(commissionAmount);
        purchase.setTotalAmount(totalAmount);
        purchase.setInstallmentAmount(installmentAmount);
        purchase.setPurchaseDate(LocalDate.now());
        purchase.setPaymentDueDates(paymentDueDates);

        purchase = purchaseRepository.save(purchase);

        return new PurchaseResponse(
                purchase.getId(),
                purchase.getPaymentScheme(),
                purchase.getTotalAmount(),
                purchase.getCommissionAmount(),
                purchase.getInstallmentAmount(),
                purchase.getPaymentDueDates()
        );
    }

    @Override
    public String assignPaymentScheme(String name, Long clientId) {
        if (name.startsWith("C") || name.startsWith("L") || name.startsWith("H")) {
            return "Scheme 1";
        } else if (clientId > 25) {
            return "Scheme 2";
        }

        return "Scheme 2";
    }

    @Override
    public List<LocalDate> generatePaymentDueDates(LocalDate startDate, int numberOfPayments, String frequency) {
        List<LocalDate> dueDates = new ArrayList<>();
        LocalDate date = startDate;

        for (int i = 1; i <= numberOfPayments; i++) {
            if ("Biweekly".equalsIgnoreCase(frequency)) {
                date = date.plusWeeks(2);
            }

            dueDates.add(date);
        }

        return dueDates;
    }
}
