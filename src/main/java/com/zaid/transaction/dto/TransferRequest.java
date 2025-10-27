package com.zaid.transaction.dto;

import jakarta.validation.constraints.*;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record TransferRequest (
    @NotBlank
    @Size(min = 8, max = 8, message = "Nomor Akun harus 8 digit")
    String sourceAccountNumber,
    @NotBlank(message = "Nomor Akun Tujuan Harus Diisi")
    @Size(min = 8, max = 8, message = "Nomor Akun harus 8 digit")
    String targetAccountNumber,
    @NotNull(message = "ISI nominal yang ingin di transfer.")
    @Positive(message = "Minimal jumlah transfer Rp. 1")
    BigDecimal amount,
    String description
    )   {}

