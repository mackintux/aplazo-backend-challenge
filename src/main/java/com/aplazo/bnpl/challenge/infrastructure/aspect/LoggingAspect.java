package com.aplazo.bnpl.challenge.infrastructure.aspect;

import com.aplazo.bnpl.challenge.application.service.RequestLogService;

import com.aplazo.bnpl.challenge.util.Loggable;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

/**
 * Aspect for logging requests and responses for methods annotated with {@link Loggable}.
 * It intercepts the annotated methods, logs their execution time, and persists the log
 * information using the {@link RequestLogService}.
 */
@Aspect
@Component
@Slf4j
public class LoggingAspect {

    private final RequestLogService requestLogService;
    private final HttpServletRequest httpServletRequest;

    /**
     * Constructor for {@link LoggingAspect}.
     *
     * @param requestLogService the service responsible for persisting request logs
     * @param httpServletRequest the HTTP servlet request to obtain metadata about the request
     */
    public LoggingAspect(RequestLogService requestLogService, HttpServletRequest httpServletRequest) {
        this.requestLogService = requestLogService;
        this.httpServletRequest = httpServletRequest;
    }

    /**
     * Intercepts methods annotated with {@link Loggable} and logs the method name, execution duration,
     * and request details.
     *
     * @param joinPoint the join point representing the intercepted method
     * @param loggable the {@link Loggable} annotation containing metadata for the intercepted method
     * @return the result of the intercepted method
     * @throws Throwable if the intercepted method throws an exception
     */
    @Around("@annotation(loggable)")
    public Object logRequest(ProceedingJoinPoint joinPoint, Loggable loggable) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object response = null;

        // Extract method information
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Loggable annotation = signature.getMethod().getAnnotation(Loggable.class);
        String methodName = annotation.method();

        // If method name is not specified in the annotation, use the method signature
        if (methodName.isEmpty()) {
            methodName = signature.toShortString();
        }

        try {
            // Proceed with the original method execution
            response = joinPoint.proceed();
        } catch (Throwable throwable) {
            log.error("Error durante la ejecución del método", throwable);
            throw throwable; // Rethrow the exception to maintain method behavior
        } finally {
            // Calculate execution duration
            long duration = System.currentTimeMillis() - startTime;

            // Log request details
            requestLogService.logRequest(
                    httpServletRequest.getRemoteAddr(), // Client IP address
                    httpServletRequest.getMethod(), // HTTP method (GET, POST, etc.)
                    methodName, // Method being invoked
                    null, // Request payload (can be extended to capture the payload)
                    response != null ? response.toString() : null, // Response payload
                    duration // Duration in milliseconds
            );

            // Log to console
            log.info("Request logged: {} with duration: {}ms", methodName, duration);
        }

        return response; // Return the original method's result
    }

}
