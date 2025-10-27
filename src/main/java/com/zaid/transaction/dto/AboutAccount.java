package com.zaid.transaction.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record AboutAccount(
    String accountNumber,
    String holderName,
    BigDecimal balance
){}
