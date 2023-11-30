package com.ss.challenge.todolist.api.http;

import com.ss.challenge.todolist.domain.items.exceptions.ItemInForbiddenStatusException;
import org.slf4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * This class contains all exception handling logic for the API.
 * It is responsible to translate thrown exceptions to well-structured API responses.
 * Each method annotated with `@ExceptionHandler` is responsible for handling related exceptions.
 **/
@RestControllerAdvice
public class ExceptionHandlerAdvice extends ResponseEntityExceptionHandler {

    private final Logger logger;

    public ExceptionHandlerAdvice(Logger logger) {
        this.logger = logger;
    }

    @ExceptionHandler(ItemInForbiddenStatusException.class)
    public ResponseEntity<Object> handleItemInForbiddenStatus(
            ItemInForbiddenStatusException exception,
            HttpHeaders headers,
            WebRequest request
    ) {
        logger.error("Error processing request", exception);

        HttpStatus forbidden = HttpStatus.FORBIDDEN;
        ProblemDetail body = ProblemDetail.forStatusAndDetail(forbidden, exception.getMessage());
        return createResponseEntity(body, headers, forbidden, request);
    }
}
