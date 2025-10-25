package com.zaid.transaction.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Builder
@Getter
public class RegistrationResponse {
    private String status;
    private String message;
    private String fullName;
    private String email;
    private String accountNumber;
    private BigDecimal initialBalance;
}
