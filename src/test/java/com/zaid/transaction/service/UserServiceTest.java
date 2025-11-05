package com.zaid.transaction.service;

import com.zaid.transaction.dto.AdminRegistrationRequest;
import com.zaid.transaction.dto.AdminRegistrationResponse;
import com.zaid.transaction.dto.ClientRegistrationRequest;
import com.zaid.transaction.dto.ClientRegistrationResponse;
import com.zaid.transaction.model.Account;
import com.zaid.transaction.model.Profile;
import com.zaid.transaction.repository.AccountRepository;
import com.zaid.transaction.security.entity.Role;
import com.zaid.transaction.security.entity.User;
import com.zaid.transaction.security.repository.RoleRepository;
import com.zaid.transaction.security.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

//@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class UserServiceTest {

    @Mock
    private AccountRepository accountRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserService userService;

    @Test
    void createAdminAccount_Success() {
        AdminRegistrationRequest request = new AdminRegistrationRequest(
                "Sapi",
                "Moo",
                "sapi1234",
                "12345678",
                "123456"
        );

        String encodedPin = "hashedPinValue";
        when(passwordEncoder.encode(request.initialPin())).thenReturn(encodedPin);

        Role adminRole = new Role();
        adminRole.setName(Role.RoleType.ROLE_ADMIN);
        when(roleRepository.findByName(Role.RoleType.ROLE_ADMIN)).thenReturn(Optional.of(adminRole));

        AdminRegistrationResponse response = userService.createAdminAccount(request);
        assertEquals("SUCCESS", response.status());
        assertEquals("Registrasi berhasil! Akun anda telah dibuat.", response.message());
        assertEquals("Sapi Moo", response.fullName());
        assertEquals("Catat Username Untuk Login (sapi1234)", response.username());
        assertEquals("12345678", response.accountNumber());

        verify(userRepository, times(1)).save(any(User.class));

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();
        assertEquals(encodedPin, savedUser.getPinNumber());
    }

    @WithMockUser(username = "usernameAdmin")
    @Test
    void createUser_Success() {
        ClientRegistrationRequest request = new ClientRegistrationRequest(
                "12345678",
                "Kambing",
                "Mbee",
                "kambing1",
                "123456",
                "87654321"
        );
        String hashedPin = "thisIsHashedPin";

        User userAdmin = new User();
        userAdmin.setUsername("usernameAdmin");
        Profile profileAdmin = new Profile();
        profileAdmin.setUser(userAdmin);
        Account accountAdmin = new Account();
        accountAdmin.setAccountNumber("12345678");
        accountAdmin.setProfile(profileAdmin);

        userAdmin.setProfile(profileAdmin);
        profileAdmin.setAccount(accountAdmin);

        when(userRepository.findByUsername("usernameAdmin")).thenReturn(Optional.of(userAdmin));

        when(accountRepository.findByAccountNumber("12345678"))
                .thenReturn(Optional.of(accountAdmin));

        when(accountRepository.findByAccountNumber("87654321"))
                .thenReturn(Optional.empty());

        Role roleClient = new Role();
        roleClient.setName(Role.RoleType.ROLE_CLIENT);
        when(roleRepository.findByName(Role.RoleType.ROLE_CLIENT))
                .thenReturn(Optional.of(roleClient));

        when(passwordEncoder.encode(request.initialPin()))
                .thenReturn(hashedPin);

        ClientRegistrationResponse response = userService.createUserAccount(request);
        BigDecimal balance = new BigDecimal(0);
        assertEquals("SUCCESS", response.status());
        assertEquals("Registrasi berhasil! Akun anda telah dibuat.", response.message());
        assertEquals("Kambing Mbee", response.fullName());
        assertEquals("Catat Username Untuk Login (kambing1)", response.username());
        assertEquals("87654321", response.accountNumber());
        assertEquals(balance, response.initialBalance());
    }
}
