package com.aplazo.bnpl.challenge.application.service;

import com.aplazo.bnpl.challenge.domain.model.Client;
import com.aplazo.bnpl.challenge.dto.ClientRequest;
import com.aplazo.bnpl.challenge.dto.ClientResponse;
import com.aplazo.bnpl.challenge.infrastructure.repository.ClientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service responsible for managing clients.
 */
@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;

    public ClientServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Transactional
    @Override
    public ClientResponse registerClient(ClientRequest request) {
        int age = request.getBirthDate().until(java.time.LocalDate.now()).getYears();

        if (age < 18 || age > 65) {
            throw new IllegalArgumentException("Client age must be between 18 and 65");
        }

        int creditLine;

        if (age >= 18 && age <= 25) {
            creditLine = 3000;
        } else if (age >= 26 && age <= 30) {
            creditLine = 5000;
        } else {
            creditLine = 8000;
        }

        Client client = new Client();

        client.setName(request.getName());
        client.setBirthDate(request.getBirthDate());
        client.setCreditLine(creditLine);

        client = clientRepository.save(client);

        return new ClientResponse(client.getId(), client.getCreditLine());
    }

}
