package com.zaid.transaction.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class TransferRequest {

    private String sourceAccountNumber;
    private String targetAccountNumber;
    private BigDecimal amount;
    private String pinNumber;
    private String description;
}
