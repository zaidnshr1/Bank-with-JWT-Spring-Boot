package com.zaid.transaction.config;

import com.zaid.transaction.dto.TransferResponse;
import com.zaid.transaction.exception.AccountNotFoundException;
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
        return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidPinException.class)
    public ResponseEntity<TransferResponse> handleInvalidPinException(InvalidPinException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(InvalidTransactionException.class)
    public ResponseEntity<TransferResponse> handleInvalidTransactionException(InvalidTransactionException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<TransferResponse> buildErrorResponse(String message, HttpStatus httpStatus) {
        TransferResponse errorTransferResponse = TransferResponse.builder()
                .status("FAILED")
                .message(message)
                .build();
        return ResponseEntity.status(httpStatus).body(errorTransferResponse);
    }

}
