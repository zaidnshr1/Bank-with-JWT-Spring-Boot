package com.zaid.transaction.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record DepositMoney (
    @NotBlank
    String accountNumber,
    @NotNull
    @Positive(message = "Minimal jumlah deposit Rp. 1")
    BigDecimal amount
){}
