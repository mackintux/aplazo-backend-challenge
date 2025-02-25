package com.aplazo.bnpl.challenge.application.service;

import com.aplazo.bnpl.challenge.dto.ClientRequest;
import com.aplazo.bnpl.challenge.dto.ClientResponse;

/**
 * Service responsible for managing clients.
 */
public interface ClientService {

    ClientResponse registerClient(ClientRequest request);

}
