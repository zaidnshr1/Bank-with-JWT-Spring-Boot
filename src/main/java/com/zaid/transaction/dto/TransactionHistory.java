package com.zaid.transaction.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record TransactionHistory (

    LocalDateTime transactionDate,
    BigDecimal amount,
    String counterPartyAccount,
    String counterPartyName,
    String description

) {}