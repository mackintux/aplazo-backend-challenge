package com.aplazo.bnpl.challenge.application.service;

import com.aplazo.bnpl.challenge.dto.ClientRequest;
import com.aplazo.bnpl.challenge.dto.ClientResponse;
import com.aplazo.bnpl.challenge.domain.model.Client;
import com.aplazo.bnpl.challenge.infrastructure.repository.ClientRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Assertions;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;

import static org.mockito.Mockito.*;

class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientServiceImpl clientService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerClient_ValidAge_ShouldAssignCorrectCredit() {
        ClientRequest request = new ClientRequest();
        request.setName("John Doe");
        request.setBirthDate(LocalDate.of(1995, 5, 10)); // 28 years old

        Client client = new Client(null, "John Doe", request.getBirthDate(), 5000);

        when(clientRepository.save(any(Client.class))).thenReturn(client);

        ClientResponse response = clientService.registerClient(request);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(5000, response.getCreditLine());
    }

    @Test
    void registerClient_InvalidAge_ShouldThrowException() {
        ClientRequest request = new ClientRequest();
        request.setName("Jane Doe");
        request.setBirthDate(LocalDate.of(2010, 5, 10)); // 13 years old

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            clientService.registerClient(request);
        });
    }
}
