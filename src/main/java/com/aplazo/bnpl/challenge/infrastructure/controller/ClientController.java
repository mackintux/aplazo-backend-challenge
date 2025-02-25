package com.aplazo.bnpl.challenge.infrastructure.controller;

import com.aplazo.bnpl.challenge.application.service.ClientService;
import com.aplazo.bnpl.challenge.dto.ClientRequest;
import com.aplazo.bnpl.challenge.dto.ClientResponse;
import com.aplazo.bnpl.challenge.util.Loggable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping("/register")
    @Loggable(method = "POST /api/clients/register")
    public ResponseEntity<ClientResponse> registerClient(@RequestBody ClientRequest request) {
        return ResponseEntity.ok(clientService.registerClient(request));
    }

}
