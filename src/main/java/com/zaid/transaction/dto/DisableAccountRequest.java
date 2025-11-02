package com.zaid.transaction.dto;

import jakarta.validation.constraints.NotBlank;

public record DisableAccountRequest(
        @NotBlank(message = "Username Must Be Filled.")
        String username,
        @NotBlank(message = "Pin Must Be Filled.")
        String pin
) {
}
