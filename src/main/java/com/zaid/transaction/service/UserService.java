package com.zaid.transaction.service;

import com.zaid.transaction.dto.AdminRegistrationRequest;
import com.zaid.transaction.dto.AdminRegistrationResponse;
import com.zaid.transaction.dto.ClientRegistrationRequest;
import com.zaid.transaction.dto.ClientRegistrationResponse;
import com.zaid.transaction.exception.AccountAlreadyExistException;
import com.zaid.transaction.exception.AccountNotFoundException;
import com.zaid.transaction.exception.RoleNotFoundException;
import com.zaid.transaction.exception.UnauthorizedAccessException;
import com.zaid.transaction.model.Account;
import com.zaid.transaction.model.Profile;
import com.zaid.transaction.repository.AccountRepository;
import com.zaid.transaction.security.entity.Role;
import com.zaid.transaction.security.entity.User;
import com.zaid.transaction.security.repository.RoleRepository;
import com.zaid.transaction.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ClientRegistrationResponse createUserAccount(ClientRegistrationRequest requestRegistration) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Account fetchingAccount = accountRepository.findByAccountNumber(requestRegistration.adminAccountNumber())
                .orElseThrow(() -> new AccountNotFoundException(requestRegistration.adminAccountNumber()));
        if(!fetchingAccount.getProfile().getUser().getUsername().equals(username)) {
            throw new UnauthorizedAccessException("Anda Tidak Memiliki Akses Ke Rekening Ini.");
        }
        Optional<Account> checkingAccountExisting = accountRepository.findByAccountNumber(requestRegistration.preferredAccountNumber());
        if(checkingAccountExisting.isPresent()) {
            throw new AccountAlreadyExistException(requestRegistration.preferredAccountNumber());
        }
        String hashedPin = passwordEncoder.encode(requestRegistration.initialPin());

        Profile registProfile = new Profile();
        registProfile.setFirstName(requestRegistration.firstName());
        registProfile.setLastName(requestRegistration.lastName());

        Account creatingAccount = new Account();
        String accountNumber = requestRegistration.preferredAccountNumber();
        creatingAccount.setAccountNumber(accountNumber);
        creatingAccount.setBalance(BigDecimal.ZERO);
        creatingAccount.setHolderName(registProfile.getFirstName() + " " + registProfile.getLastName());

        User settingUser = new User();
        settingUser.setUsername(requestRegistration.username());
        settingUser.setPinNumber(hashedPin);
        settingUser.setEnabled(true);

        Role defineClientRole = roleRepository.findByName(Role.RoleType.ROLE_CLIENT)
                .orElseThrow(() -> new RoleNotFoundException("Role Client Tidak Ada Di Database"));
        Set<Role> roles = new HashSet<>();
        roles.add(defineClientRole);

        settingUser.setRoles(roles);
        settingUser.setProfile(registProfile);
        registProfile.setUser(settingUser);
        registProfile.setAccount(creatingAccount);
        creatingAccount.setProfile(registProfile);

//        profileRepository.save(registProfile);
//        accountRepository.save(creatingAccount);
        userRepository.save(settingUser);

        return ClientRegistrationResponse.builder()
                .status("SUCCESS")
                .message("Registrasi berhasil! Akun anda telah dibuat.")
                .fullName(creatingAccount.getHolderName())
                .username("Catat Username Untuk Login (" + settingUser.getUsername() + (")"))
                .accountNumber(creatingAccount.getAccountNumber())
                .initialBalance(creatingAccount.getBalance())
                .build();
    }

    @Transactional
    public AdminRegistrationResponse createAdminAccount(AdminRegistrationRequest requestRegistration) {
        String hashedPin = passwordEncoder.encode(requestRegistration.initialPin());

        Profile registProfile = new Profile();
        registProfile.setFirstName(requestRegistration.firstName());
        registProfile.setLastName(requestRegistration.lastName());

        Account creatingAccount = new Account();
        creatingAccount.setAccountNumber(requestRegistration.preferredAccountNumber());
        creatingAccount.setHolderName(registProfile.getFirstName() + " " + registProfile.getLastName());

        User settingUser = new User();
        settingUser.setUsername(requestRegistration.username());
        settingUser.setPinNumber(hashedPin);
        settingUser.setEnabled(true);

        Role defineAdminRole = roleRepository.findByName(Role.RoleType.ROLE_ADMIN)
                .orElseThrow(() -> new RoleNotFoundException("Role Admin Tidak Ada Di Database"));
        Set<Role> roles = new HashSet<>();
        roles.add(defineAdminRole);

        settingUser.setRoles(roles);
        settingUser.setProfile(registProfile);
        registProfile.setUser(settingUser);
        registProfile.setAccount(creatingAccount);
        creatingAccount.setProfile(registProfile);

        userRepository.save(settingUser);

        return AdminRegistrationResponse.builder()
                .status("SUCCESS")
                .message("Registrasi berhasil! Akun anda telah dibuat.")
                .fullName(creatingAccount.getHolderName())
                .username("Catat Username Untuk Login (" + settingUser.getUsername() + (")"))
                .accountNumber(requestRegistration.preferredAccountNumber())
                .build();
    }
}
