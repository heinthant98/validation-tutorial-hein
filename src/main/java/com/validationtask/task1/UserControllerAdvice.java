package com.validationtask.task1;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.*;
import java.util.stream.Collectors;

@RestControllerAdvice
public class UserControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<Map<String, Object>> errors = new ArrayList<>();
        e.getBindingResult().getFieldErrors().forEach(fieldError -> {
            Map<String, Object> error = new HashMap<>();
            error.put("field", fieldError.getField());
            error.put("message", e.getBindingResult().getFieldErrors(fieldError.getField()).stream().map(FieldError::getDefaultMessage).collect(Collectors.toList()));
            errors.add(error);
        });
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, "validation error",
                errors.stream().distinct().collect(Collectors.toList()));
        return ResponseEntity.badRequest().body(errorResponse);
    }
    
    public static final class ErrorResponse {
        private HttpStatus httpStatus;
        private String message;
        private List<Map<String, Object>> errors;

        public ErrorResponse(HttpStatus httpStatus, String message, List<Map<String, Object>> errors) {
            this.httpStatus = httpStatus;
            this.message = message;
            this.errors = errors;
        }

        public HttpStatus getHttpStatus() {
            return httpStatus;
        }

        public String getMessage() {
            return message;
        }

        public List<Map<String, Object>> getErrors() {
            return errors;
        }
    }

}

