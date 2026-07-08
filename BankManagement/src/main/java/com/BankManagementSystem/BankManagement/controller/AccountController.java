package com.BankManagementSystem.BankManagement.controller;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.util.List;

import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.BankManagementSystem.BankManagement.dto.account.AccountResponse;
import com.BankManagementSystem.BankManagement.dto.account.CreateAccountRequest;
import com.BankManagementSystem.BankManagement.dto.account.DashboardResponse;
import com.BankManagementSystem.BankManagement.dto.transaction.TransactionHistoryResponse;
import com.BankManagementSystem.BankManagement.dto.transaction.TransactionRequest;
import com.BankManagementSystem.BankManagement.dto.transaction.TransactionResponse;
import com.BankManagementSystem.BankManagement.dto.transaction.TransferRequest;
import com.BankManagementSystem.BankManagement.services.AccountService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/accounts")
@Tag(name = "Account Management", description = "Operations related to bank accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<AccountResponse> getAccount(
            @PathVariable String accountNumber) {

        return ResponseEntity.ok(
                accountService.getAccounts(accountNumber)
        );
    }

    @GetMapping
    public ResponseEntity<List<AccountResponse>> getAllAccounts(Authentication authentication) {

        return ResponseEntity.ok(
                accountService.getAllAccounts(authentication.getName())
        );
    }

    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(
            @RequestBody CreateAccountRequest request,
            Authentication authentication) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(accountService.createAccount(request, authentication.getName()));
    }

    @Operation(summary = "Deposit Money")
    @PostMapping("/deposit")
    public ResponseEntity<TransactionResponse> deposit(
            @RequestBody TransactionRequest request,
            Authentication authentication) {

        return ResponseEntity.ok(
                accountService.deposit(request, authentication.getName())
        );
    }
     @Operation(summary = "Withdraw Money")
    @PostMapping("/withdraw")
    public ResponseEntity<TransactionResponse> withdraw(
            @RequestBody TransactionRequest request,
            Authentication authentication) {

        return ResponseEntity.ok(
                accountService.withdraw(request, authentication.getName())
        );
    }

     @Operation(summary = "Tranfer Money")
    @PostMapping("/transfer")
    public ResponseEntity<TransactionResponse> transfer(
            @RequestBody @Valid TransferRequest request,
            Authentication authentication) {

        return ResponseEntity.ok(
                accountService.transfer(request, authentication.getName())
        );
    }

        @Operation(summary = "Transaction History")
        @GetMapping("/{accountNumber}/transactions")
        public ResponseEntity<Page<TransactionHistoryResponse>> getTransactionHistory(
        @PathVariable String accountNumber,
        Authentication authentication,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "5") int size) {

    return ResponseEntity.ok(
            accountService.getTransactionHistory(
                    accountNumber,
                    authentication.getName(),
                    page,
                    size));
}



@Operation(summary = "Mini Statement")
@GetMapping("/{accountNumber}/mini-statement")
public ResponseEntity<List<TransactionHistoryResponse>> getMiniStatement(
        @PathVariable String accountNumber,
        Authentication authentication) {

    return ResponseEntity.ok(
            accountService.getMiniStatement(
                    accountNumber,
                    authentication.getName()));


}

                @Operation(summary = "Download Statement")
                @GetMapping("/{accountNumber}/statement")
                public ResponseEntity<InputStreamResource> downloadStatement(

                        @PathVariable String accountNumber,

                        @RequestParam LocalDate from,

                        @RequestParam LocalDate to,

                        Authentication authentication) {

                ByteArrayInputStream pdf =
                        accountService.downloadStatement(
                                accountNumber,
                                from,
                                to,
                                authentication.getName());

                HttpHeaders headers = new HttpHeaders();

                headers.add(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=Account_Statement.pdf");

                return ResponseEntity.ok()
                        .headers(headers)
                        .contentType(MediaType.APPLICATION_PDF)
                        .body(new InputStreamResource(pdf));
                }


                @Operation(summary = "Dashboard")
                @GetMapping("/{accountNumber}/dashboard")
                public ResponseEntity<DashboardResponse> getDashboard(
                        @PathVariable String accountNumber,
                        Authentication authentication) {

                return ResponseEntity.ok(
                        accountService.getDashboard(
                                accountNumber,
                                authentication.getName()));
                }
}