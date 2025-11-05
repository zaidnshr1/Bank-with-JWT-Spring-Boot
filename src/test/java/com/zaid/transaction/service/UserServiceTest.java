package com.zaid.transaction.service;

import com.zaid.transaction.dto.AdminRegistrationRequest;
import com.zaid.transaction.dto.AdminRegistrationResponse;
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
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
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
}
