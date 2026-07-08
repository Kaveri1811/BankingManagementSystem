package com.BankManagementSystem.BankManagement.dto.account;

import java.math.BigDecimal;

import com.BankManagementSystem.BankManagement.enums.AccountStatus;
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
public class AccountResponse {

    private Long id;

    private String accountNumber;

    private AccountType accountType;

    private BigDecimal balance;

    private AccountStatus status;
}