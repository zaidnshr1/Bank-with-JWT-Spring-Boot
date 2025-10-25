package com.zaid.transaction.controller;

import com.zaid.transaction.dto.RegistrationRequest;
import com.zaid.transaction.dto.RegistrationResponse;
import com.zaid.transaction.dto.TransferRequest;
import com.zaid.transaction.dto.TransferResponse;
import com.zaid.transaction.service.AccountService;
import com.zaid.transaction.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/banking")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private RegistrationService registrationService;

    @PostMapping("/transfer")
    public ResponseEntity<TransferResponse> performTransfer(@RequestBody TransferRequest requestingTransfer) {
        TransferResponse respondingTheTranfer = accountService.transferMoney(requestingTransfer);
        return ResponseEntity.ok(respondingTheTranfer);
    }

    @PostMapping("/register")
    public ResponseEntity<RegistrationResponse> register(@RequestBody RegistrationRequest registrationRequest) {
        RegistrationResponse respondingRegistration = registrationService.registerNewUser(registrationRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(respondingRegistration);
    }
}
