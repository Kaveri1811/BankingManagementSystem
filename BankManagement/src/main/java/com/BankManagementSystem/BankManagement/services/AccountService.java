package com.BankManagementSystem.BankManagement.services;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;

import com.BankManagementSystem.BankManagement.dto.account.AccountResponse;
import com.BankManagementSystem.BankManagement.dto.account.CreateAccountRequest;
import com.BankManagementSystem.BankManagement.dto.account.DashboardResponse;
import com.BankManagementSystem.BankManagement.dto.transaction.TransactionHistoryResponse;
import com.BankManagementSystem.BankManagement.dto.transaction.TransactionRequest;
import com.BankManagementSystem.BankManagement.dto.transaction.TransactionResponse;
import com.BankManagementSystem.BankManagement.dto.transaction.TransferRequest;
import com.BankManagementSystem.BankManagement.enums.TransactionType;

public interface AccountService {

    AccountResponse createAccount(CreateAccountRequest request, String username);

    AccountResponse getAccounts(String accountNumber);

    List<AccountResponse> getAllAccounts(String username);

    TransactionResponse deposit(TransactionRequest request, String username);

    TransactionResponse withdraw(TransactionRequest request, String username);

    TransactionResponse transfer(TransferRequest request, String username);

    Page<TransactionHistoryResponse> getTransactionHistory(
        String accountNumber,
        String username,
        int page,
        int size);

        Page<TransactionHistoryResponse> getTransactionHistory(
        String accountNumber,
        String username,
        TransactionType type,
        int page,
        int size);

    List<TransactionHistoryResponse> getMiniStatement( String accountNumber,
                String username);

                ByteArrayInputStream downloadStatement(
        String accountNumber,
        LocalDate from,
        LocalDate to,
        String username);

        DashboardResponse getDashboard(
        String accountNumber,
        String username);
}