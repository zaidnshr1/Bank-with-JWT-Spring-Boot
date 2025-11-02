package com.zaid.transaction.service;

import com.zaid.transaction.dto.*;
import com.zaid.transaction.exception.AccountNotFoundException;
import com.zaid.transaction.exception.InvalidPinException;
import com.zaid.transaction.exception.UnauthorizedAccessException;
import com.zaid.transaction.model.Account;
import com.zaid.transaction.model.Profile;
import com.zaid.transaction.repository.AccountRepository;
import com.zaid.transaction.repository.ProfileRepository;
import com.zaid.transaction.security.entity.User;
import com.zaid.transaction.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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

    @Transactional
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public DisableAccountResponse disableAccount(DisableAccountRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User gettingUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new AccountNotFoundException(request.username()));
        if(!passwordEncoder.matches(request.pin(), gettingUser.getPinNumber())) {
            throw new InvalidPinException();
        }
        if (!gettingUser.getUsername().equals(username)) {
            throw new UnauthorizedAccessException("Anda Tidak Memiliki Akses Ke Rekening Ini.");
        }
        gettingUser.setEnabled(false);
        userRepository.save(gettingUser);
        return DisableAccountResponse.builder()
                .message("Berhasil Disable Akun.")
                .build();
    }
}
