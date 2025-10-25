package com.zaid.transaction.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class TransferResponse {
    private String status;
    private String message;
    private Long transactionId;
    private LocalDateTime transactionDate;
    private BigDecimal transferredAmount;
    private String sourceAccount;
    private String targetAccount;
}
