package com.zaid.transaction.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(

        @NotBlank(message = "Email cannot be blank")
        @Email(message = "Invalid Email format")
        String email,

        @NotBlank(message = "PIN Number cannot be blank")
        @Size(min = 6, max = 6, message = "PIN Number must be 6 digits")
        String pinNumber
) {
}
