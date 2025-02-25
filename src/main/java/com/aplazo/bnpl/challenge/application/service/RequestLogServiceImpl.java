package com.aplazo.bnpl.challenge.application.service;

import com.aplazo.bnpl.challenge.domain.model.RequestLog;
import com.aplazo.bnpl.challenge.infrastructure.repository.RequestLogRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service responsible for managing request logs.
 */
@Service
public class RequestLogServiceImpl implements RequestLogService {

    /**
     * Repository for performing CRUD operations on `RequestLog` entities.
     */
    private final RequestLogRepository requestLogRepository;

    /**
     * Constructor to initialize the repository dependency.
     *
     * @param requestLogRepository The repository for managing `RequestLog` entities.
     */
    public RequestLogServiceImpl(RequestLogRepository requestLogRepository) {
        this.requestLogRepository = requestLogRepository;
    }

    /**
     * Logs a request by creating and saving a `RequestLog` entity.
     *
     * @param ip             The IP address of the requester.
     * @param httpMethod     The HTTP method used in the request (e.g., GET, POST).
     * @param method         The method name being logged.
     * @param requestPayload The payload sent in the request.
     * @param responsePayload The payload received in the response.
     * @param duration       The duration of the request in milliseconds.
     */
    @Override
    public void logRequest(String ip, String httpMethod, String method, String requestPayload, String responsePayload, long duration) {
        RequestLog requestLog = new RequestLog();

        requestLog.setRequestDate(LocalDateTime.now());
        requestLog.setIp(ip != null ? ip : "127.0.0.1");
        requestLog.setHttpMethod(httpMethod);
        requestLog.setMethod(method);
        requestLog.setRequestPayload(requestPayload);
        requestLog.setResponsePayload(responsePayload);
        requestLog.setDuration(duration);

        requestLogRepository.save(requestLog);
    }

    /**
     * Retrieves all logged requests.
     *
     * @return A list of all `RequestLog` entities.
     */
    @Override
    public List<RequestLog> getAllLogs() {
        return requestLogRepository.findAll();
    }

}
