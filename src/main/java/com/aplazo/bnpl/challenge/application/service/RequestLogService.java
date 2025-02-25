package com.aplazo.bnpl.challenge.application.service;

import com.aplazo.bnpl.challenge.domain.model.RequestLog;

import java.util.List;

/**
 * Service responsible for managing request logs.
 */
public interface RequestLogService {

    /**
     * Log a new request.
     *
     * @param ip              the IP address of the request
     * @param httpMethod      the HTTP method used (GET, POST, etc.)
     * @param method          the method that was called
     * @param requestPayload  the payload of the request
     * @param responsePayload the payload of the response
     * @param duration        the duration of the request in milliseconds
     */
    void logRequest(String ip, String httpMethod, String method, String requestPayload, String responsePayload, long duration);

    /**
     * Retrieve all logged requests.
     *
     * @return a list of all RequestLogs
     */
    List<RequestLog> getAllLogs();

}
