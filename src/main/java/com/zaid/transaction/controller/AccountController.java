package com.zaid.transaction.controller;

import com.zaid.transaction.dto.*;
import com.zaid.transaction.service.TransactionService;
import com.zaid.transaction.service.TransferService;
import com.zaid.transaction.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/banking")
public class AccountController {

    @Autowired
    private TransferService accountService;

    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/transfer")
    public ResponseEntity<TransferResponse> performTransfer(@RequestBody TransferRequest requestingTransfer) {
        TransferResponse respondingTheTranfer = accountService.transferMoney(requestingTransfer);
        return ResponseEntity.ok(respondingTheTranfer);
    }

    @PostMapping("/register")
    public ResponseEntity<RegistrationResponse> register(@RequestBody RegistrationRequest registrationRequest) {
        RegistrationResponse respondingRegistration = registrationService.registerNewUser(registrationRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(respondingRegistration);
    }

    @GetMapping("/history/{accountNumber}")
    public ResponseEntity<Page<TransactionHistory>> getHistory(
                        @PathVariable String accountNumber,
                        @RequestParam(value = "0") int page,
                        @RequestParam(value = "10") int size) {
        Page<TransactionHistory> transactionHistory = transactionService.getTransactionHistory(accountNumber, page, size);
        return ResponseEntity.ok(transactionHistory);
    }
}
