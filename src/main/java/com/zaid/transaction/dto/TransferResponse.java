package com.zaid.transaction.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record TransferResponse (

    String status,
    String message,
    Long transactionId,
    LocalDateTime transactionDate,
    BigDecimal transferredAmount,
    String sourceAccount,
    String targetAccount

) {}