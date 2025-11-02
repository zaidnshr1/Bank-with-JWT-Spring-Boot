package com.zaid.transaction.service;

import com.zaid.transaction.dto.DepositMoneyRequest;
import com.zaid.transaction.dto.DepositMoneyResponse;
import com.zaid.transaction.dto.TransactionHistory;
import com.zaid.transaction.exception.AccountNotFoundException;
import com.zaid.transaction.exception.UnauthorizedAccessException;
import com.zaid.transaction.model.Account;
import com.zaid.transaction.model.Transaction;
import com.zaid.transaction.repository.AccountRepository;
import com.zaid.transaction.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    @PreAuthorize("hasRole('CLIENT')")
    public Page<TransactionHistory> getTransactionHistory(String accountNumber, int page, int size) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Account gettingAccount = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException(accountNumber));

        if(!gettingAccount.getProfile().getUser().getUsername().equals(username)) {
            throw new UnauthorizedAccessException("Anda Tidak Memiliki Akses Ke Rekening Ini.");
        }

        Pageable pageable = PageRequest.of(page, size);

        Page<Transaction> pageTransaction = transactionRepository.findBySourceAccountOrTargetAccount(accountNumber, pageable);

        return pageTransaction.map(transaction -> {
            boolean gettingSourceAccount = transaction.getSourceAccount() != null && transaction.getSourceAccount().getAccountNumber().equals(accountNumber);
            Account gettingCounterPartyAccount = gettingSourceAccount ? transaction.getTargetAccount() : transaction.getSourceAccount();

            return TransactionHistory.builder()
                    .transactionDate(transaction.getTransactionDate())
                    .amount(transaction.getAmount())
                    .counterPartyAccount(gettingCounterPartyAccount != null ? gettingCounterPartyAccount.getAccountNumber() : "Self Deposit")
                    .counterPartyName(gettingCounterPartyAccount != null ? gettingCounterPartyAccount.getHolderName() : "Self Deposit")
                    .description(transaction.getDescription())
                    .build();
        });
    }

    @PreAuthorize("hasRole('CLIENT')")
    @Transactional
    public DepositMoneyResponse depositMoney(DepositMoneyRequest moneyRequest) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Account gettingAccount = accountRepository.findByAccountNumber(moneyRequest.accountNumber())
                .orElseThrow(() -> new AccountNotFoundException(moneyRequest.accountNumber()));

        if(!gettingAccount.getProfile().getUser().getUsername().equals(username)) {
            throw new UnauthorizedAccessException("Anda Tidak Memiliki Akses Ke Rekening Ini.");
        }

        gettingAccount.setBalance(gettingAccount.getBalance().add(moneyRequest.amount()));
        accountRepository.save(gettingAccount);

        Transaction transaction = new Transaction();
        transaction.setAmount(moneyRequest.amount());
        transaction.setDescription("DEPOSIT BERHASIL");
        transaction.setTargetAccount(gettingAccount);
        transaction.setSourceAccount(null);
        transactionRepository.save(transaction);

        return DepositMoneyResponse.builder()
                .status("SUCCESS")
                .message("DEPOSIT BERHASIL DILAKUKAN")
                .accountNumber("Nomor Akun " + gettingAccount.getAccountNumber())
                .fullName(gettingAccount.getHolderName())
                .amount(moneyRequest.amount())
                .build();

    }

}
