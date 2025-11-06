package com.zaid.transaction.controller;

import com.zaid.transaction.dto.AdminRegistrationRequest;
import com.zaid.transaction.dto.AdminRegistrationResponse;
import com.zaid.transaction.dto.AuthRequest;
import com.zaid.transaction.dto.AuthResponse;
import com.zaid.transaction.security.service.JwtService;
import com.zaid.transaction.service.UserService;
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

    @PostMapping("/register-admin")
    public ResponseEntity<AdminRegistrationResponse> regist(@RequestBody @Valid AdminRegistrationRequest request) {
        AdminRegistrationResponse response = userService.createAdminAccount(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
