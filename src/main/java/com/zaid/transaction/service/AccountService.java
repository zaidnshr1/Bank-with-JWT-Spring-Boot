package com.zaid.transaction.service;

import com.zaid.transaction.dto.AboutAccount;
import com.zaid.transaction.dto.UpdateProfileRequest;
import com.zaid.transaction.dto.UpdateProfileResponse;
import com.zaid.transaction.exception.AccountNotFoundException;
import com.zaid.transaction.exception.UnauthorizedAccessException;
import com.zaid.transaction.model.Account;
import com.zaid.transaction.model.Profile;
import com.zaid.transaction.repository.AccountRepository;
import com.zaid.transaction.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final ProfileRepository profileRepository;

    @PreAuthorize("hasAuthority('ROLE_CLIENT')")
    public AboutAccount getAboutAccount(String accountNumber) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Account getAccount = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException(accountNumber));
        if(!getAccount.getProfile().getUser().getUsername().equals(username)) {
            throw new UnauthorizedAccessException("Anda Tidak Memiliki Akses Ke Rekening Ini.");
        }
        return AboutAccount.builder()
                .accountNumber(getAccount.getAccountNumber())
                .holderName(getAccount.getHolderName())
                .balance(getAccount.getBalance())
                .build();
    }

    @PreAuthorize("hasAuthority('ROLE_CLIENT')")
    public UpdateProfileResponse updateProfile(UpdateProfileRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Account gettingAccount = accountRepository.findByAccountNumber(request.accountNumber())
                .orElseThrow(() -> new AccountNotFoundException(request.accountNumber()));
        if(!gettingAccount.getProfile().getUser().getUsername().equals(username)) {
            throw new UnauthorizedAccessException("Anda Tidak Memiliki Akses Ke Rekening Ini.");
        }
        Profile updatingProfile = gettingAccount.getProfile();
        updatingProfile.setFirstName(request.firstName());
        updatingProfile.setLastName(request.lastName());
        gettingAccount.setHolderName(updatingProfile.getFirstName() + " " + updatingProfile.getLastName());
        profileRepository.save(updatingProfile);
        return UpdateProfileResponse.builder()
                .message("SUCCESS")
                .fullname(updatingProfile.getFirstName() + " " + updatingProfile.getLastName())
                .build();
    }
}
