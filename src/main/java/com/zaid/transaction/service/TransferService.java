package com.zaid.transaction.service;

import com.zaid.transaction.dto.TransferRequest;
import com.zaid.transaction.dto.TransferResponse;
import com.zaid.transaction.exception.AccountNotFoundException;
import com.zaid.transaction.exception.InvalidTransactionException;
import com.zaid.transaction.exception.UnauthorizedAccessException;
import com.zaid.transaction.model.Account;
import com.zaid.transaction.model.Transaction;
import com.zaid.transaction.repository.AccountRepository;
import com.zaid.transaction.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class TransferService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    @Transactional
    @PreAuthorize("hasRole('CLIENT')")
    public TransferResponse transferMoney(TransferRequest request) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Account gettingAccount = accountRepository.findByAccountNumber(request.sourceAccountNumber())
                .orElseThrow(() -> new AccountNotFoundException(request.sourceAccountNumber()));

        if(!gettingAccount.getProfile().getUser().getUsername().equals(username)) {
            throw new UnauthorizedAccessException("Anda Tidak Memiliki Akses Ke Rekening Ini.");
        }

        String sourceAccNum = request.sourceAccountNumber();
        String targetAccountNum = request.targetAccountNumber();
        BigDecimal amount = request.amount();

        if (sourceAccNum.equals(targetAccountNum)) {
            throw new InvalidTransactionException("Tidak dapat transfer ke akun yang sama.");
        }

        Account sourceAccount = accountRepository.findByAccountNumber(request.sourceAccountNumber())
                .orElseThrow(() -> new AccountNotFoundException(sourceAccNum));
        Account targetAccount = accountRepository.findByAccountNumber(request.targetAccountNumber())
                .orElseThrow(() -> new AccountNotFoundException(targetAccountNum));

        if (sourceAccount.getBalance().compareTo(amount) < 0) {
            throw new InvalidTransactionException("Saldo tidak mencukupi. Saldo saat ini " + sourceAccount.getBalance());
        }

        BigDecimal minBalance = new BigDecimal(15000);
        if (sourceAccount.getBalance().subtract(amount).compareTo(minBalance) < 0) {
            throw new InvalidTransactionException("Sisa saldo tidak boleh kurang dari Rp 15,000");
        }

        sourceAccount.setBalance(sourceAccount.getBalance().subtract(amount));
        targetAccount.setBalance(targetAccount.getBalance().add(amount));

        accountRepository.save(sourceAccount);
        accountRepository.save(targetAccount);

        Transaction newTransaction = new Transaction();
        newTransaction.setSourceAccount(sourceAccount);
        newTransaction.setTargetAccount(targetAccount);
        newTransaction.setAmount(amount);;
        newTransaction.setDescription(request.description());

        Transaction savedTransaction = transactionRepository.save(newTransaction);

        return TransferResponse.builder()
                .status("SUCCESS")
                .message("Transfer Uang Berhasil")
                .transactionId(savedTransaction.getId())
                .transactionDate(savedTransaction.getTransactionDate())
                .transferredAmount(amount)
                .sourceAccount(sourceAccNum)
                .targetAccount(targetAccountNum)
                .build();
    }
}
