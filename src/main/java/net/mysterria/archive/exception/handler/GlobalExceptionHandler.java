package net.mysterria.archive.exception.handler;

import net.mysterria.archive.exception.model.DuplicateResourceException;
import net.mysterria.archive.exception.model.ResourceNotFoundException;
import net.mysterria.archive.exception.model.ValidationException;
import net.mysterria.archive.exception.response.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
            ResourceNotFoundException ex, WebRequest request) {
        String uri = request.getDescription(false).replace("uri=", "");
        logger.warn("Resource not found at {}: {}", uri, ex.getMessage());

        ErrorResponse error = new ErrorResponse(
                ex.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                "Not Found",
                uri
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateResourceException(
            DuplicateResourceException ex, WebRequest request) {
        String uri = request.getDescription(false).replace("uri=", "");
        logger.warn("Duplicate resource conflict at {}: {}", uri, ex.getMessage());

        ErrorResponse error = new ErrorResponse(
                ex.getMessage(),
                HttpStatus.CONFLICT.value(),
                "Conflict",
                uri
        );
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            ValidationException ex, WebRequest request) {
        String uri = request.getDescription(false).replace("uri=", "");
        logger.warn("Validation error at {}: {}", uri, ex.getMessage());

        ErrorResponse error = new ErrorResponse(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                uri
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex, WebRequest request) {
        String uri = request.getDescription(false).replace("uri=", "");
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));

        logger.warn("Method argument validation failed at {}: {}", uri, message);

        ErrorResponse error = new ErrorResponse(
                message,
                HttpStatus.BAD_REQUEST.value(),
                "Validation Error",
                uri
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(
            Exception ex, WebRequest request) {
        String uri = request.getDescription(false).replace("uri=", "");

        logger.error("Internal server error occurred at {}: {}", uri, ex.getMessage(), ex);

        ErrorResponse error = new ErrorResponse(
                "Internal server error occurred",
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                uri
        );
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}