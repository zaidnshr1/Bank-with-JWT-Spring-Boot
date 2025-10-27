package com.zaid.transaction.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record RegistrationResponse(
    String status,
    String message,
    String fullName,
    String email,
    String accountNumber,
    BigDecimal initialBalance
) {}