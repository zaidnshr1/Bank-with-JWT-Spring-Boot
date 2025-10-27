package com.zaid.transaction.service;

import com.zaid.transaction.dto.RegistrationRequest;
import com.zaid.transaction.dto.RegistrationResponse;
import com.zaid.transaction.exception.InvalidInputException;
import com.zaid.transaction.model.Account;
import com.zaid.transaction.model.Profile;
import com.zaid.transaction.repository.AccountRepository;
import com.zaid.transaction.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final ProfileRepository profileRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public RegistrationResponse registerNewUser(RegistrationRequest requestRegistration) {

        String hashedPin = passwordEncoder.encode(requestRegistration.initialPin());

        Profile registProfile = new Profile();
        registProfile.setFirstName(requestRegistration.firstName());
        registProfile.setLastName(requestRegistration.lastName());
        registProfile.setEmail(requestRegistration.email());
        registProfile.setPinNumber(hashedPin);

        Profile savedProfile = profileRepository.save(registProfile);

        Account creatingAccount = new Account();
        String accountNumber = requestRegistration.preferredAccountNumber();
        if (accountNumber == null || accountNumber.isEmpty() || accountRepository.findByAccountNumber(accountNumber).isPresent()) {
            accountNumber = generateUniqueAccountNumber();
        }

        creatingAccount.setAccountNumber(accountNumber);
        creatingAccount.setBalance(BigDecimal.ZERO);
        creatingAccount.setProfile(savedProfile);

        creatingAccount.setHolderName(registProfile.getFirstName() + " " + registProfile.getLastName());

        Account savedAccount = accountRepository.save(creatingAccount);

        return RegistrationResponse.builder()
                .status("SUCCESS")
                .message("Registrasi berhasil! Akun anda telah dibuat.")
                .fullName(savedProfile.getFirstName() + " " + savedProfile.getLastName())
                .email(savedProfile.getEmail())
                .accountNumber(savedAccount.getAccountNumber())
                .initialBalance(savedAccount.getBalance())
                .build();
    }

    private String generateUniqueAccountNumber() {
        return "ACC-" + UUID.randomUUID().toString().substring(0,8).toUpperCase();
    }

}
