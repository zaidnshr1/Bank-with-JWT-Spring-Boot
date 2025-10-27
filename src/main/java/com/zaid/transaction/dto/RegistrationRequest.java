package com.zaid.transaction.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record RegistrationRequest (
    @NotBlank(message = "Nama Depan harus diisi.")
    String firstName,
    @NotBlank(message = "Nama Belakang harus diisi.")
    String lastName,
    @Email
    String email,
    @Size(min = 6, max = 6, message = "PIN Harus berupa 6 karakter.")
    @NotBlank
    String initialPin,
    @Size(min = 8, max = 8, message = "Nomor Akun Harus berupa 8 karakter.")
    String preferredAccountNumber
)
    {}
