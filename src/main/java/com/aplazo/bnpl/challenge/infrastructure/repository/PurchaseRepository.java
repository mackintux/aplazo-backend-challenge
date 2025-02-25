package com.aplazo.bnpl.challenge.infrastructure.repository;

import com.aplazo.bnpl.challenge.domain.model.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing `Purchase` entities.
 * Extends JpaRepository to provide CRUD operations.
 */
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
}
