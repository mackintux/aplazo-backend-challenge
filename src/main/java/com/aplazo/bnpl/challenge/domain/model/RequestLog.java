package com.aplazo.bnpl.challenge.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Represents a log entry for requests made to the application.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "requests")
public class RequestLog {

    /**
     * The unique identifier for the request log.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The IP address from which the request originated.
     */
    private String ip;

    /**
     * The timestamp when the request was made.
     */
    private LocalDateTime requestDate;

    /**
     * The method name of the service handling the request.
     */
    private String method;

    /**
     * The HTTP method (e.g., GET, POST) used for the request.
     */
    private String httpMethod;

    /**
     * The duration of the request in milliseconds.
     */
    private Long duration;

    /**
     * The payload of the request.
     * Stored as a large object (LOB) to accommodate large data sizes.
     */
    @Lob
    private String requestPayload;

    /**
     * The payload of the response.
     * Stored as a large object (LOB) to accommodate large data sizes.
     */
    @Lob
    private String responsePayload;

}
