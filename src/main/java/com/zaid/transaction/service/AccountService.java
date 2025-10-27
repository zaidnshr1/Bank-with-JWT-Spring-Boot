package com.zaid.transaction.service;

import com.zaid.transaction.dto.AboutAccount;
import com.zaid.transaction.exception.AccountNotFoundException;
import com.zaid.transaction.exception.UnauthorizedAccessException;
import com.zaid.transaction.model.Account;
import com.zaid.transaction.repository.AccountRepository;
import com.zaid.transaction.security.service.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final SecurityUtil securityUtil;

    public AboutAccount getAboutAccount(String accountNumber) {

        Account getAccount = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException(accountNumber));

        Long loggedInProfileId = securityUtil.getLoggedInProfileId();
        if (!getAccount.getId().equals(loggedInProfileId)) {
            throw new UnauthorizedAccessException("Anda tidak memiliki akses ke akun " + accountNumber);
        }

        return AboutAccount.builder()
                .accountNumber(getAccount.getAccountNumber())
                .holderName(getAccount.getHolderName())
                .balance(getAccount.getBalance())
                .build();
    }
}
