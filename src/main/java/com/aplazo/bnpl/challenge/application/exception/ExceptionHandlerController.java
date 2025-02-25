package com.aplazo.bnpl.challenge.application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

/**
 * Global exception handler for handling specific exceptions across the application.
 */
@RestControllerAdvice
public class ExceptionHandlerController {

    /**
     * Handles `HttpClientErrorException` thrown during REST calls.
     *
     * @param ex the exception
     * @return a user-friendly error message
     */
    @ExceptionHandler(HttpClientErrorException.class)
    public String handleHttpClientErrorException(HttpClientErrorException ex) {
        if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
            return "Page not found";
        }

        return "An error occurred: " + ex.getMessage();
    }

}
