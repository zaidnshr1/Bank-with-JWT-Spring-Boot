package com.zaid.transaction.service;

import com.zaid.transaction.dto.TransferRequest;
import com.zaid.transaction.dto.TransferResponse;
import com.zaid.transaction.exception.AccountNotFoundException;
import com.zaid.transaction.exception.InvalidPinException;
import com.zaid.transaction.exception.InvalidTransactionException;
import com.zaid.transaction.model.Account;
import com.zaid.transaction.model.Profile;
import com.zaid.transaction.model.Transaction;
import com.zaid.transaction.repository.AccountRepository;
import com.zaid.transaction.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class TransferService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public TransferResponse transferMoney(TransferRequest request) {
        String sourceAccNum = request.getSourceAccountNumber();
        String targetAccountNum = request.getTargetAccountNumber();
        BigDecimal amount = request.getAmount();

        if (sourceAccNum.equals(targetAccountNum)) {
            throw new InvalidTransactionException("Tidak dapat transfer ke akun yang sama.");
        }

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidTransactionException("Jumlah transfer harus positif.");
        }

        Account sourceAccount = accountRepository.findByAccountNumber(request.getSourceAccountNumber())
                .orElseThrow(() -> new AccountNotFoundException(sourceAccNum));
        Account targetAccount = accountRepository.findByAccountNumber(request.getTargetAccountNumber())
                .orElseThrow(() -> new AccountNotFoundException(targetAccountNum));

        Profile sourceProfile = sourceAccount.getProfile();

        if (!passwordEncoder.matches(request.getPinNumber(), sourceProfile.getPinNumber())) {
            throw new InvalidPinException();
        }

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
        newTransaction.setDescription(request.getDescription());

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
