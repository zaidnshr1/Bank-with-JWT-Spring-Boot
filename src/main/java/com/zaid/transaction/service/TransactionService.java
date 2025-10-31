package com.zaid.transaction.service;

import com.zaid.transaction.dto.DepositMoneyResponse;
import com.zaid.transaction.dto.TransactionHistory;
import com.zaid.transaction.exception.AccountNotFoundException;
import com.zaid.transaction.exception.UnauthorizedAccessException;
import com.zaid.transaction.model.Account;
import com.zaid.transaction.model.Transaction;
import com.zaid.transaction.repository.AccountRepository;
import com.zaid.transaction.repository.TransactionRepository;
import com.zaid.transaction.security.service.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final SecurityUtil securityUtil;

    public Page<TransactionHistory> getTransactionHistory(String accountNumber, int page, int size) {

        Account gettingAccount = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException(accountNumber));

        Long loggedInProfileId = securityUtil.getLoggedInProfileId();

        if (!gettingAccount.getId().equals(loggedInProfileId)) {
            throw new UnauthorizedAccessException("Anda tidak memiliki akses untuk melihat riwayat akun " + accountNumber);
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

    @Transactional
    public DepositMoneyResponse depositMoney(String accountNumber, BigDecimal amount) {
        Account gettingAccount = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException(accountNumber));
        Long loggedInProfileId = securityUtil.getLoggedInProfileId();

        if (!gettingAccount.getProfile().getId().equals(loggedInProfileId)) {
            throw new UnauthorizedAccessException("Anda hanya dapat melakukan deposit ke akun Anda sendiri.");
        }

        gettingAccount.setBalance(gettingAccount.getBalance().add(amount));
        accountRepository.save(gettingAccount);

        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setDescription("DEPOSIT BERHASIL");
        transaction.setTargetAccount(gettingAccount);
        transaction.setSourceAccount(null);
        transactionRepository.save(transaction);

        return DepositMoneyResponse.builder()
                .status("SUCCESS")
                .message("DEPOSIT BERHASIL DILAKUKAN")
                .accountNumber("Nomor Akun " + gettingAccount.getAccountNumber())
                .fullName(gettingAccount.getHolderName())
                .amount(amount)
                .build();

    }

}
