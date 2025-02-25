package com.aplazo.bnpl.challenge.infrastructure.repository;

import com.aplazo.bnpl.challenge.domain.model.RequestLog;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing `RequestLog` entities.
 * Extends JpaRepository to provide CRUD operations.
 */
public interface RequestLogRepository extends JpaRepository<RequestLog, Long> {
}
