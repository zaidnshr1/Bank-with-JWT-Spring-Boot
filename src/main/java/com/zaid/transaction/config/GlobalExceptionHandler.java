package com.zaid.transaction.config;

import com.zaid.transaction.dto.RegistrationResponse;
import com.zaid.transaction.dto.TransferResponse;
import com.zaid.transaction.exception.AccountNotFoundException;
import com.zaid.transaction.exception.InvalidInputException;
import com.zaid.transaction.exception.InvalidPinException;
import com.zaid.transaction.exception.InvalidTransactionException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<TransferResponse> handleAccountNotFoundException(AccountNotFoundException ex) {
        return buildErrorTransferResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
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

}
