package com.zaid.transaction.controller;

import com.zaid.transaction.dto.*;
import com.zaid.transaction.security.service.UserService;
import com.zaid.transaction.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
public class AdministrationController {

    private final UserService userService;
    private final AccountService accountService;

    @PostMapping("/register-user")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ClientRegistrationResponse> regist(@RequestBody @Valid ClientRegistrationRequest request) {
        ClientRegistrationResponse response = userService.createUserAccount(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/disable-account")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<DisableAccountResponse> disableAccount(@RequestBody @Valid DisableAccountRequest disableRequest) {
        DisableAccountResponse responseDiableAccount = accountService.disableAccount(disableRequest);
        return ResponseEntity.ok(responseDiableAccount);
    }
}
