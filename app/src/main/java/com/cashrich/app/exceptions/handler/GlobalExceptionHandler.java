package com.cashrich.app.exceptions.handler;

import com.cashrich.app.dao.response.APIResponse;
import com.cashrich.app.exceptions.ExceptionClass;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<APIResponse<Object>> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request) {
        Map<String, String> errors = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        fieldError -> fieldError.getField(),
                        fieldError -> fieldError.getDefaultMessage(),
                        (message1, message2) -> message1
                ));

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new APIResponse<>(HttpStatus.BAD_REQUEST, false, errors, "Validation failed"));
    }

    @ExceptionHandler({ExceptionClass.UserAlreadyExistsException.class})
    public ResponseEntity<APIResponse<Object>> handleStudentAlreadyExistsException(ExceptionClass.UserAlreadyExistsException exception) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new APIResponse<>(HttpStatus.CONFLICT, false, "", exception.getMessage()));
    }

    @ExceptionHandler({ExceptionClass.UserNotFoundException.class})
    public ResponseEntity<APIResponse<Object>> handleStudentAlreadyExistsException(ExceptionClass.UserNotFoundException exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new APIResponse<>(HttpStatus.NOT_FOUND, false, "", exception.getMessage()));
    }

    @ExceptionHandler({BadCredentialsException.class})
    public ResponseEntity<APIResponse<Object>> handleStudentAlreadyExistsException(BadCredentialsException exception) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new APIResponse<>(HttpStatus.UNAUTHORIZED, false, "", exception.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<APIResponse<Object>> handleAllExceptions(Exception ex, WebRequest request) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new APIResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, false, null, ex.getMessage()));
    }

    //When the request to the API fails
    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<APIResponse<Object>> handleHttpClientErrorException(HttpClientErrorException exception) {
        return ResponseEntity
                .status(exception.getStatusCode())
                .body(new APIResponse<>((HttpStatus) exception.getStatusCode(), false, "", exception.getResponseBodyAsString()));
    }

    //When there is a connection issue.
    @ExceptionHandler(ResourceAccessException.class)
    public ResponseEntity<APIResponse<Object>> handleResourceAccessException(ResourceAccessException exception) {
        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(new APIResponse<>(HttpStatus.SERVICE_UNAVAILABLE, false, "", "Service Unavailable: " + exception.getMessage()));
    }

    //issue while saving data to the database.
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<APIResponse<Object>> handleDataAccessException(DataAccessException exception) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new APIResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, false, "", "Database Error: " + exception.getMessage()));
    }
}
