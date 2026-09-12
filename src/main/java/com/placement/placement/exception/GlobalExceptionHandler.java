package com.placement.placement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1️⃣ Validation Errors (400)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> validationErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                validationErrors.put(error.getField(),
                        error.getDefaultMessage()));
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", 400);
        response.put("errors", validationErrors);
        return response;
    }

    // 2️⃣ Student Not Found (404)
    @ExceptionHandler(StudentNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, Object> handleStudentNotFound(
            StudentNotFoundException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", 404);
        response.put("message", ex.getMessage());
        return response;
    }

    // 3️⃣ Company Not Found (404)
    @ExceptionHandler(CompanyNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, Object> handleCompanyNotFound(
            CompanyNotFoundException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", 404);
        response.put("message", ex.getMessage());
        return response;
    }

    // 4️⃣ Placement Not Found (404)
    @ExceptionHandler(PlacementNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, Object> handlePlacementNotFound(
            PlacementNotFoundException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", 404);
        response.put("message", ex.getMessage());
        return response;
    }

    // 5️⃣ Resource Not Found (404)
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, Object> handleResourceNotFound(
            ResourceNotFoundException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", 404);
        response.put("message", ex.getMessage());
        return response;
    }

    // 6️⃣ Bad Input / Validation (400)
    // Used for: wrong file type, empty file, invalid input
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleIllegalArgument(
            IllegalArgumentException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", 400);
        response.put("message", ex.getMessage());
        return response;
    }

    // 7️⃣ Duplicate / Conflict (409)
    // Used for: duplicate email, duplicate company name, duplicate application
    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, Object> handleIllegalState(
            IllegalStateException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", 409);
        response.put("message", ex.getMessage());
        return response;
    }

    // 8️⃣ Generic Exception (500)
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, Object> handleGeneralException(Exception ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", 500);
        response.put("message", "Something went wrong");
        response.put("details", ex.getMessage());
        return response;
    }
}