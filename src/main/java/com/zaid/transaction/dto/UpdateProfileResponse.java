package com.zaid.transaction.dto;

import lombok.Builder;

@Builder
public record UpdateProfileResponse(
        String message,
        String fullname
) {
}
