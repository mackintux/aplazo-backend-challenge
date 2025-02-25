package com.aplazo.bnpl.challenge.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseResponse {

    private Long id;

    private String paymentScheme;

    private BigDecimal totalAmount;

    private BigDecimal commissionAmount;

    private BigDecimal installmentAmount;

    private List<LocalDate> paymentDueDates;

}
