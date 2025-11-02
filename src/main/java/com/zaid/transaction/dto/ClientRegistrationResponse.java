package com.zaid.transaction.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ClientRegistrationResponse(
    String status,
    String message,
    String fullName,
    String username,
    String accountNumber,
    BigDecimal initialBalance
) {}