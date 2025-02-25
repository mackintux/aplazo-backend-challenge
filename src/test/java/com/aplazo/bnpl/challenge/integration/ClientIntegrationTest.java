package com.aplazo.bnpl.challenge.integration;

import com.aplazo.bnpl.challenge.dto.ClientRequest;
import com.aplazo.bnpl.challenge.dto.ClientResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ClientIntegrationTest {

    @Container
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("db_test")
            .withUsername("user")
            .withPassword("pass");

    @LocalServerPort
    private int port;

    private final RestTemplate restTemplate = new RestTemplate();

    @Test
    void registerClient_ShouldReturnValidResponse() {
        ClientRequest request = new ClientRequest();
        request.setName("John Doe");
        request.setBirthDate(LocalDate.of(1995, 5, 10));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<ClientRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<ClientResponse> response = restTemplate.exchange(
                "http://localhost:" + port + "/api/clients/register",
                HttpMethod.POST,
                entity,
                ClientResponse.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(5000, response.getBody().getCreditLine());
    }
}
