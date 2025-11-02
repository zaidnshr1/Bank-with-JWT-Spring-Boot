package com.zaid.transaction.controller;

import com.zaid.transaction.dto.*;
import com.zaid.transaction.service.AccountService;
import com.zaid.transaction.service.TransactionService;
import com.zaid.transaction.service.TransferService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/account")
public class AccountController {

    private final TransferService transferService;
    private final TransactionService transactionService;
    private final AccountService accountService;

    @PostMapping("/transfer")
    @PreAuthorize("hasAuthority('ROLE_CLIENT')")
    public ResponseEntity<TransferResponse> performTransfer(@RequestBody @Valid TransferRequest requestingTransfer) {
        TransferResponse respondingTheTranfer = transferService.transferMoney(requestingTransfer);
        return ResponseEntity.ok(respondingTheTranfer);
    }

    @GetMapping("/history/{accountNumber}")
    @PreAuthorize("hasAuthority('ROLE_CLIENT')")
    public ResponseEntity<Page<TransactionHistory>> getHistory(
                        @PathVariable String accountNumber,
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size) {
        Page<TransactionHistory> transactionHistory = transactionService.getTransactionHistory(accountNumber, page, size);
        return ResponseEntity.ok(transactionHistory);
    }

    @GetMapping("/{accountNumber}")
    @PreAuthorize("hasAuthority('ROLE_CLIENT')")
    public ResponseEntity<AboutAccount> getAccountDetails(@PathVariable String accountNumber) {
        AboutAccount aboutAccount = accountService.getAboutAccount(accountNumber);
        return ResponseEntity.ok(aboutAccount);
    }

    @PostMapping("/deposit")
    @PreAuthorize("hasAuthority('ROLE_CLIENT')")
    public ResponseEntity<DepositMoneyResponse> depositMoney(@RequestBody @Valid DepositMoneyRequest depositRequest) {
        DepositMoneyResponse depositMoney = transactionService.depositMoney(depositRequest);
        return ResponseEntity.ok(depositMoney);
    }
}
