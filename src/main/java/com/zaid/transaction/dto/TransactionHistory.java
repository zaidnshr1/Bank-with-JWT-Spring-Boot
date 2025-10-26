package com.zaid.transaction.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
public class TransactionHistory {

    private LocalDateTime transactionDate;
    private BigDecimal amount;
    private String counterPartyAccount;
    private String counterPartyName;
    private String description;

}
