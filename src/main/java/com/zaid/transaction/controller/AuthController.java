package com.zaid.transaction.controller;

import com.zaid.transaction.dto.AuthRequest;
import com.zaid.transaction.dto.AuthResponse;
import com.zaid.transaction.dto.RegistrationRequest;
import com.zaid.transaction.dto.RegistrationResponse;
import com.zaid.transaction.security.entity.User;
import com.zaid.transaction.security.service.JwtService;
import com.zaid.transaction.security.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final JwtService jwtService;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid AuthRequest authRequest) {
        String token = jwtService.generateToken(authRequest.username());
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("/register")
    public ResponseEntity<RegistrationResponse> regist(@RequestBody @Valid RegistrationRequest request) {
        RegistrationResponse response = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
