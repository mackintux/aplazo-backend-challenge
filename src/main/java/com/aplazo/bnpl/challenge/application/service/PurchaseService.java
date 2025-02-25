package com.aplazo.bnpl.challenge.application.service;

import com.aplazo.bnpl.challenge.dto.PurchaseRequest;
import com.aplazo.bnpl.challenge.dto.PurchaseResponse;

import java.time.LocalDate;
import java.util.List;

/**
 * Service responsible for managing purchases.
 */
public interface PurchaseService {

    PurchaseResponse registerPurchase(PurchaseRequest request);

    String assignPaymentScheme(String name, Long clientId);

    List<LocalDate> generatePaymentDueDates(LocalDate startDate, int numberOfPayments, String frequency);

}
