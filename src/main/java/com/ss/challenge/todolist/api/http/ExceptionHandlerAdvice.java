package com.ss.challenge.todolist.api.http;

import com.ss.challenge.todolist.domain.items.exceptions.ItemInForbiddenStatusException;
import com.ss.challenge.todolist.domain.items.exceptions.ItemWithDueDateInThePastException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
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

    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<Object> handleNotFound(RuntimeException exception, WebRequest request) {
        String message = exception.getMessage() != null ? exception.getMessage() : "";
        ProblemDetail body = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, message);
        return handleExceptionInternal(exception, body, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler({ItemInForbiddenStatusException.class, ItemWithDueDateInThePastException.class, IllegalArgumentException.class})
    public ResponseEntity<ProblemDetail> handleUnprocessableEntity(Exception exception, WebRequest request) {
        return getObjectResponseEntity(HttpStatus.UNPROCESSABLE_ENTITY, exception, new HttpHeaders());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ProblemDetail> onConstraintValidationException(ConstraintViolationException exception, HttpHeaders headers) {
        ProblemDetail body = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
        exception
                .getConstraintViolations()
                .forEach(violation -> body.setProperty(violation.getPropertyPath().toString(), violation.getMessage()));
        return new ResponseEntity<>(body, headers, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ProblemDetail body = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
        exception
                .getBindingResult()
                .getFieldErrors()
                .forEach(fieldError -> body.setProperty(fieldError.getField(), fieldError.getDefaultMessage()));

        return new ResponseEntity<>(body, exception.getHeaders(), HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<ProblemDetail> getObjectResponseEntity(HttpStatus statusCode, Exception exception, HttpHeaders headers) {
        logger.error("Error processing request", exception);
        String message = exception.getMessage() != null ? exception.getMessage() : "";
        ProblemDetail body = ProblemDetail.forStatusAndDetail(statusCode, message);
        return new ResponseEntity<>(body, headers, statusCode);
    }
}
