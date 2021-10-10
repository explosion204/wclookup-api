package com.explosion204.wclookup.controller;

import com.explosion204.wclookup.exception.EntityNotFoundException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.*;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
public class ApplicationExceptionHandler {
    private static final String ERROR_DELIMITER = "; ";
    private static final String ENTITY_NOT_FOUND_MESSAGE = "entity_not_found";
    private static final String INVALID_ENTITY_MESSAGE = "invalid_entity";
    private static final String ERROR_MESSAGE = "errorMessage";

    private final ResourceBundleMessageSource messageSource;

    public ApplicationExceptionHandler(ResourceBundleMessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleNotFound() {
        String errorMessage = getErrorMessage(ENTITY_NOT_FOUND_MESSAGE);
        return buildErrorResponseEntity(NOT_FOUND, errorMessage);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException e) {
        List<String> violations = e.getConstraintViolations()
                        .stream().map(ConstraintViolation::getMessage)
                        .toList();
        String violationsString = String.join(ERROR_DELIMITER, violations);

        String errorMessage = String.format(getErrorMessage(INVALID_ENTITY_MESSAGE), violationsString);
        return buildErrorResponseEntity(BAD_REQUEST, errorMessage);
    }

    private String getErrorMessage(String errorMessageName) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(errorMessageName, null, locale);
    }

    private ResponseEntity<Object> buildErrorResponseEntity(HttpStatus status, String errorMessage) {
        Map<String, Object> body = new HashMap<>();
        body.put(ERROR_MESSAGE, errorMessage);

        return new ResponseEntity<>(body, status);
    }
}
