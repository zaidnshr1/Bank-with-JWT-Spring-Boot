package com.zaid.transaction.service;

import com.zaid.transaction.dto.RegistrationRequest;
import com.zaid.transaction.dto.RegistrationResponse;
import com.zaid.transaction.exception.InvalidInputException;
import com.zaid.transaction.model.Account;
import com.zaid.transaction.model.Profile;
import com.zaid.transaction.repository.AccountRepository;
import com.zaid.transaction.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class RegistrationService {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public RegistrationResponse registerNewUser(RegistrationRequest requestRegistration) {

        if (requestRegistration.getInitialPin() == null || requestRegistration.getInitialPin().length() > 6 || requestRegistration.getInitialPin().length() < 6) {
            throw new InvalidInputException("PIN Harus berupa 6 karakter");
        }

        String hashedPin = passwordEncoder.encode(requestRegistration.getInitialPin());

        Profile registProfile = new Profile();
        registProfile.setFirstName(requestRegistration.getFirstName());
        registProfile.setLastName(requestRegistration.getLastName());
        registProfile.setEmail(requestRegistration.getEmail());
        registProfile.setPinNumber(hashedPin);

        Profile savedProfile = profileRepository.save(registProfile);

        Account creatingAccount = new Account();
        String accountNumber = requestRegistration.getPreferredAccountNumber();
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
