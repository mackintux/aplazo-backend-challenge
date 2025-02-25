package com.aplazo.bnpl.challenge.infrastructure.controller;

import com.aplazo.bnpl.challenge.application.service.PurchaseService;
import com.aplazo.bnpl.challenge.dto.PurchaseRequest;
import com.aplazo.bnpl.challenge.dto.PurchaseResponse;
import com.aplazo.bnpl.challenge.util.Loggable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/purchases")
public class PurchaseController {

    private final PurchaseService purchaseService;

    public PurchaseController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    @PostMapping("/register")
    @Loggable(method = "POST /api/purchases/register")
    public ResponseEntity<PurchaseResponse> registerPurchase(@RequestBody PurchaseRequest request) {
        return ResponseEntity.ok(purchaseService.registerPurchase(request));
    }

}
