package com.zaid.transaction.exception;

import com.zaid.transaction.dto.DepositMoneyResponse;
import com.zaid.transaction.dto.RegistrationResponse;
import com.zaid.transaction.dto.TransferResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler  {

    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<Object> handleUnauthorizedAccessException(UnauthorizedAccessException ex) {
        return new ResponseEntity<>(
                createErrorBody("Akses Ditolak", ex.getMessage()),
                HttpStatus.FORBIDDEN
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidPinException.class)
    public ResponseEntity<TransferResponse> handleInvalidPinException(InvalidPinException ex) {
        return buildErrorTransferResponse(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(InvalidTransactionException.class)
    public ResponseEntity<TransferResponse> handleInvalidTransactionException(InvalidTransactionException ex) {
        return buildErrorTransferResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<RegistrationResponse> handleInvalidInputException(InvalidInputException ex) {
        return buildErrorRegistrationResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

     @ExceptionHandler(AccountNotFoundException.class)
     public ResponseEntity<Object> handleNotFoundException(AccountNotFoundException ex) {
         return new ResponseEntity<>(
                 createErrorBody("Tidak Ditemukan", ex.getMessage()),
                 HttpStatus.NOT_FOUND
         );
     }

    private ResponseEntity<TransferResponse> buildErrorTransferResponse(String message, HttpStatus httpStatus) {
        TransferResponse errorTransferResponse = TransferResponse.builder()
                .status("FAILED")
                .message(message)
                .build();
        return ResponseEntity.status(httpStatus).body(errorTransferResponse);
    }

    private ResponseEntity<RegistrationResponse> buildErrorRegistrationResponse(String message, HttpStatus httpStatus) {
        RegistrationResponse errorRegistrationResponse = RegistrationResponse.builder()
                .status("FAILED")
                .message(message)
                .build();
        return ResponseEntity.status(httpStatus).body(errorRegistrationResponse);
    }

    record ErrorResponse(String error, String message) {}

    private Object createErrorBody(String error, String message) {
        return new ErrorResponse(error, message);
    }

}
