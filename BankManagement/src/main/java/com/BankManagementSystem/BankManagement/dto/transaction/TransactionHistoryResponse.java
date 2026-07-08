package com.BankManagementSystem.BankManagement.dto.transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.BankManagementSystem.BankManagement.enums.TransactionType;

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
public class TransactionHistoryResponse {

    private Long transactionId;

    private TransactionType transactionType;

    private BigDecimal amount;

    private String senderAccount;

    private String receiverAccount;

    private String description;

    private LocalDateTime transactionDate;
}