package com.explosion204.wclookup.controller;

import com.explosion204.wclookup.exception.EntityAlreadyExistsException;
import com.explosion204.wclookup.exception.EntityNotFoundException;
import com.explosion204.wclookup.exception.InvalidPageContextException;
import com.explosion204.wclookup.controller.debug.DebugMailLogger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@ControllerAdvice
public class ApplicationExceptionHandler {
    private static final String ERROR_DELIMITER = "; ";
    private static final String ENTITY_NOT_FOUND_MESSAGE = "entity_not_found";
    private static final String ENTITY_ALREADY_EXISTS_MESSAGE = "entity_already_exists";
    private static final String INVALID_ENTITY_MESSAGE = "invalid_entity";
    private static final String INVALID_CREDENTIALS_MESSAGE = "invalid_credentials";
    private static final String INVALID_PAGE_NUMBER_MESSAGE = "invalid_page_number";
    private static final String INVALID_PAGE_SIZE_MESSAGE = "invalid_page_size";
    private static final String INTERNAL_SERVER_ERROR_MESSAGE = "internal_server_error";
    private static final String ERROR_MESSAGE = "errorMessage";

    private static final Logger logger = LogManager.getLogger();

    private final ResourceBundleMessageSource messageSource;
    private DebugMailLogger debugMailLogger;

    public ApplicationExceptionHandler(ResourceBundleMessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Autowired(required = false)
    public void setMailLogger(DebugMailLogger debugMailLogger) {
        this.debugMailLogger = debugMailLogger;
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleNotFound(EntityNotFoundException e) {
        String causeEntityName = e.getCauseEntity().getSimpleName();
        String errorMessage = String.format(getErrorMessage(ENTITY_NOT_FOUND_MESSAGE), causeEntityName);
        return buildErrorResponseEntity(NOT_FOUND, errorMessage);
    }

    @ExceptionHandler(EntityAlreadyExistsException.class)
    public ResponseEntity<Object> handleEntityAlreadyExists(EntityAlreadyExistsException e) {
        String causeEntityName = e.getCauseEntity().getSimpleName();
        String errorMessage = String.format(getErrorMessage(ENTITY_ALREADY_EXISTS_MESSAGE), causeEntityName);
        return buildErrorResponseEntity(CONFLICT, errorMessage);
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

    @ExceptionHandler({ BadCredentialsException.class, CredentialsExpiredException.class })
    public ResponseEntity<Object> handleAuthError() {
        String errorMessage = getErrorMessage(INVALID_CREDENTIALS_MESSAGE);
        return buildErrorResponseEntity(UNAUTHORIZED, errorMessage);
    }

    @ExceptionHandler(InvalidPageContextException.class)
    public ResponseEntity<Object> handleInvalidPageContext(InvalidPageContextException e) {
        InvalidPageContextException.ErrorType errorType = e.getErrorType();
        int invalidValue = e.getInvalidValue();

        String errorMessage = switch (errorType) {
            case INVALID_PAGE_NUMBER -> getErrorMessage(INVALID_PAGE_NUMBER_MESSAGE);
            case INVALID_PAGE_SIZE -> getErrorMessage(INVALID_PAGE_SIZE_MESSAGE);
        };

        return buildErrorResponseEntity(BAD_REQUEST, String.format(errorMessage, invalidValue));
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<Object> handleDefault(HttpServletRequest request, Exception e) {
        logger.error("Uncaught exception", e);

        if (debugMailLogger != null) {
            debugMailLogger.log(request, e);
        }

        String errorMessage = getErrorMessage(INTERNAL_SERVER_ERROR_MESSAGE);
        return buildErrorResponseEntity(INTERNAL_SERVER_ERROR, errorMessage);
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
