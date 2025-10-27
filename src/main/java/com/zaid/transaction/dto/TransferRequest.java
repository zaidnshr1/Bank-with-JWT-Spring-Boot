package com.zaid.transaction.dto;

import jakarta.validation.constraints.*;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record TransferRequest (
    @NotBlank
    String sourceAccountNumber,
    @NotBlank(message = "Nomor Akun Tujuan Harus Diisi")
    String targetAccountNumber,
    @NotNull(message = "ISI nominal yang ingin di transfer.")
    @PositiveOrZero(message = "Minimal jumlah transfer Rp. 1")
    BigDecimal amount,
    @NotBlank(message = "PIN WAJIB DIISI")
    @Size(min = 6, max = 6, message = "PIN BERUPA 6 KARAKTER.")
    String pinNumber,
    String description
    )   {}

