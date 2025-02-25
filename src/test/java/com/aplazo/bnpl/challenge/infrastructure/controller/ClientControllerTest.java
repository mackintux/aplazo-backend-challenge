package com.aplazo.bnpl.challenge.infrastructure.controller;

import com.aplazo.bnpl.challenge.application.service.ClientService;
import com.aplazo.bnpl.challenge.dto.ClientRequest;
import com.aplazo.bnpl.challenge.dto.ClientResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClientControllerTest {

    @Mock
    private ClientService clientService;

    @InjectMocks
    private ClientController clientController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerClient_ShouldReturnClientResponse() {
        // GIVEN: A `ClientRequest` and an expected response from the service
        ClientRequest request = new ClientRequest();
        ClientResponse response = new ClientResponse(1L, 5000);

        when(clientService.registerClient(any(ClientRequest.class))).thenReturn(response);

        // WHEN: The `registerClient` endpoint is called
        ResponseEntity<ClientResponse> result = clientController.registerClient(request);

        // THEN: It is validated that the response is the expected one
        assertNotNull(result);
        assertEquals(200, result.getStatusCode().value());
        assertNotNull(result.getBody());
        assertEquals(1L, result.getBody().getId());
        assertEquals(5000, result.getBody().getCreditLine());

        verify(clientService, times(1)).registerClient(request);
    }
}
