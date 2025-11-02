package com.zaid.transaction.dto;

import lombok.Builder;

@Builder
public record DisableAccountResponse(
        String message
) {
}
