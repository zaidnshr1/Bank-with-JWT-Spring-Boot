package com.zaid.transaction.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class DepositMoney {
    private String accountNumber;
    private BigDecimal amount;
}
