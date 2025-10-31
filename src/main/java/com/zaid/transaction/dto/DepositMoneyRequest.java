package com.zaid.transaction.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record DepositMoneyRequest(
    @NotBlank
    @Size(min = 8, max = 8, message = "Nomor Akun harus 8 digit")
    String accountNumber,
    @NotNull
    @Positive(message = "Minimal jumlah deposit Rp. 1")
    BigDecimal amount
){}
