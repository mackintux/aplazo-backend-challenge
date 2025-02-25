package com.aplazo.bnpl.challenge.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PurchaseRequest {

    private Long clientId;

    private BigDecimal amount;

}
