package com.aplazo.bnpl.challenge.infrastructure.repository;

import com.aplazo.bnpl.challenge.domain.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing `Client` entities.
 * Extends JpaRepository to provide CRUD operations.
 */
public interface ClientRepository extends JpaRepository<Client, Long> {
}
