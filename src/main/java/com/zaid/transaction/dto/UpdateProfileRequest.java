package com.zaid.transaction.dto;

import jakarta.validation.constraints.Size;

public record UpdateProfileRequest(
        String accountNumber,
        @Size(max = 25, message = "Nama Depan Tidak Boleh Lebih Dari 25 Karakter.")
        String firstName,
        @Size(max = 25, message = "Nama Belakang Tidak Boleh Lebih Dari 25 Karakter.")
        String lastName
) {
}
