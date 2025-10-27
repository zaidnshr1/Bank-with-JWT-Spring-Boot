package com.zaid.transaction.security.service;

import com.zaid.transaction.dto.LoginRequest;
import com.zaid.transaction.dto.LoginResponse;
import com.zaid.transaction.exception.EmailNotFoundException;
import com.zaid.transaction.model.Profile;
import com.zaid.transaction.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final ProfileRepository profileRepository;
    private final JwtService jwtService;

    public LoginResponse authenticate(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.pinNumber()
                )
        );
        Profile user = profileRepository.findByEmail(request.email())
                .orElseThrow(() -> new EmailNotFoundException("Profile not found after authentication"));
        String jwtToken = jwtService.generateToken(user);
        return new LoginResponse(jwtToken);
    }

}
