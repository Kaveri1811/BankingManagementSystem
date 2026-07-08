package com.BankManagementSystem.BankManagement.dto.transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionResponse {

    private String accountNumber;

    private String transactionType;

    private BigDecimal amount;

    private BigDecimal balance;

    private LocalDateTime transactionDate;

    private String message;
}