package com.zaid.transaction.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record DepositMoneyResponse(
        String status,
        String message,
        String accountNumber,
        String fullName,
        BigDecimal amount
) {
}
