package com.zaid.transaction.dto;

import lombok.Builder;

@Builder
public record AdminRegistrationResponse(
        String status,
        String message,
        String fullName,
        String accountNumber,
        String username
) {
}
