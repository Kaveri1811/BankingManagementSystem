package com.BankManagementSystem.BankManagement.dto.account;

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
public class CreateAccountRequest {


    private AccountType accountType;

    private String initialDeposit;
}