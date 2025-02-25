package com.aplazo.bnpl.challenge.repository;

import com.aplazo.bnpl.challenge.domain.model.Client;
import com.aplazo.bnpl.challenge.infrastructure.repository.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class ClientRepositoryTest {

    @Autowired
    private ClientRepository clientRepository;

    @BeforeEach
    void setUp() {
        // GIVEN: A client is saved in the database
        Client client = new Client();
        client.setName("John Doe");
        client.setBirthDate(LocalDate.of(1995, 5, 10));
        client.setCreditLine(5000);

        clientRepository.save(client);
    }

    @Test
    void findById_ShouldReturnClient() {
        // WHEN: We are looking for a client
        Optional<Client> result = clientRepository.findById(1L);

        // THEN: We validate that it exists
        assertTrue(result.isPresent());
        assertEquals("John Doe", result.get().getName());
        assertEquals(5000, result.get().getCreditLine());
    }

    @Test
    void findById_ShouldReturnEmpty_WhenClientNotExists() {
        // WHEN: We are looking for a non-existent ID
        Optional<Client> result = clientRepository.findById(999L);

        // THEN: Should not be found
        assertFalse(result.isPresent());
    }
}
