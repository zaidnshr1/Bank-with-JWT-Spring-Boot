package com.zaid.transaction.service;

import com.zaid.transaction.dto.TransactionHistory;
import com.zaid.transaction.exception.AccountNotFoundException;
import com.zaid.transaction.model.Account;
import com.zaid.transaction.model.Transaction;
import com.zaid.transaction.repository.AccountRepository;
import com.zaid.transaction.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    public Page<TransactionHistory> getTransactionHistory(String accountNumber, int page, int size) {

        if (accountRepository.findByAccountNumber(accountNumber).isEmpty()) {
            throw new AccountNotFoundException(accountNumber);
        }

        Pageable pageable = PageRequest.of(page, size);

        Page<Transaction> pageTransaction = transactionRepository.findBySourceAccountOrTargetAccount(accountNumber, pageable);

        return pageTransaction.map(transaction -> {
            boolean gettingSourceAccount = transaction.getSourceAccount().getAccountNumber().equals(accountNumber);
            Account gettingCounterPartyAccount = gettingSourceAccount ? transaction.getTargetAccount() : transaction.getSourceAccount();

            return TransactionHistory.builder()
                    .transactionDate(transaction.getTransactionDate())
                    .amount(transaction.getAmount())
                    .counterPartyAccount(gettingCounterPartyAccount.getAccountNumber())
                    .counterPartyName(gettingCounterPartyAccount.getHolderName())
                    .description(transaction.getDescription())
                    .build();
        });
    }
}
