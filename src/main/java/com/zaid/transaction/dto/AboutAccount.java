package com.zaid.transaction.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class AboutAccount {
    private String accountNumber;
    private String holderName;
    private BigDecimal balance;
}
