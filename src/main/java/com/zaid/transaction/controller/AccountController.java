package com.zaid.transaction.controller;

import com.zaid.transaction.dto.*;
import com.zaid.transaction.service.AccountService;
import com.zaid.transaction.service.TransactionService;
import com.zaid.transaction.service.TransferService;
import com.zaid.transaction.service.RegistrationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/banking")
public class AccountController {

    private final TransferService transferService;
    private final RegistrationService registrationService;
    private final TransactionService transactionService;
    private final AccountService accountService;

    @PostMapping("/transfer")
    public ResponseEntity<TransferResponse> performTransfer(@RequestBody @Valid TransferRequest requestingTransfer) {
        TransferResponse respondingTheTranfer = transferService.transferMoney(requestingTransfer);
        return ResponseEntity.ok(respondingTheTranfer);
    }

    @PostMapping("/register")
    public ResponseEntity<RegistrationResponse> register(@RequestBody @Valid RegistrationRequest registrationRequest) {
        RegistrationResponse respondingRegistration = registrationService.registerNewUser(registrationRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(respondingRegistration);
    }

    @GetMapping("/history/{accountNumber}")
    public ResponseEntity<Page<TransactionHistory>> getHistory(
                        @PathVariable String accountNumber,
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size) {
        Page<TransactionHistory> transactionHistory = transactionService.getTransactionHistory(accountNumber, page, size);
        return ResponseEntity.ok(transactionHistory);
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<AboutAccount> getAccountDetails(@PathVariable String accountNumber) {
        AboutAccount aboutAccount = accountService.getAboutAccount(accountNumber);
        return ResponseEntity.ok(aboutAccount);
    }

    @PostMapping("/deposit")
    public ResponseEntity<DepositMoney> depositMoney(@RequestBody @Valid DepositMoney depositRequest) {
        DepositMoney depositMoney = transactionService.depositMoney
                (depositRequest.accountNumber(), depositRequest.amount());
        return ResponseEntity.ok(depositMoney);
    }
}
