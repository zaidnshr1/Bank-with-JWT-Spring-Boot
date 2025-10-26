package com.zaid.transaction.service;

import com.zaid.transaction.dto.AboutAccount;
import com.zaid.transaction.exception.AccountNotFoundException;
import com.zaid.transaction.model.Account;
import com.zaid.transaction.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public AboutAccount getAboutAccount(String accountNumber) {

        Account getAccount = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException(accountNumber));

        return AboutAccount.builder()
                .accountNumber(getAccount.getAccountNumber())
                .holderName(getAccount.getHolderName())
                .balance(getAccount.getBalance())
                .build();
    }
}
