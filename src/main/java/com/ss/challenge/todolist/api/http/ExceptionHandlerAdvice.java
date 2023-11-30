package com.ss.challenge.todolist.api.http;

import com.ss.challenge.todolist.domain.items.exceptions.ItemInForbiddenStatusException;
import com.ss.challenge.todolist.domain.items.exceptions.ItemWithDueDateInThePastException;
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
        return getObjectResponseEntity(HttpStatus.UNPROCESSABLE_ENTITY, exception, headers, request);
    }

    @ExceptionHandler(ItemWithDueDateInThePastException.class)
    public ResponseEntity<Object> handleItemWithDueDateInThePast(
            ItemWithDueDateInThePastException exception,
            HttpHeaders headers,
            WebRequest request
    ) {
        return getObjectResponseEntity(HttpStatus.UNPROCESSABLE_ENTITY, exception, headers, request);
    }

    private ResponseEntity<Object> getObjectResponseEntity(HttpStatus status, Exception exception, HttpHeaders headers, WebRequest request) {
        logger.error("Error processing request", exception);
        ProblemDetail body = ProblemDetail.forStatusAndDetail(status, exception.getMessage());
        return createResponseEntity(body, headers, status, request);
    }
}
