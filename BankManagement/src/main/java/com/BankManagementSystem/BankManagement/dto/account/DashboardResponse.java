package com.BankManagementSystem.BankManagement.dto.account;

import java.math.BigDecimal;
import java.util.List;

import com.BankManagementSystem.BankManagement.dto.transaction.TransactionHistoryResponse;
import com.BankManagementSystem.BankManagement.enums.AccountType;

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
public class DashboardResponse {

    private String customerName;

    private String accountNumber;

    private AccountType accountType;

    private BigDecimal currentBalance;

    private List<TransactionHistoryResponse> recentTransactions;
}