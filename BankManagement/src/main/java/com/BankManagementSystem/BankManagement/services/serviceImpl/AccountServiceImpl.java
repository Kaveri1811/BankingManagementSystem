package com.BankManagementSystem.BankManagement.services.serviceImpl;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.BankManagementSystem.BankManagement.dto.account.AccountResponse;
import com.BankManagementSystem.BankManagement.dto.account.CreateAccountRequest;
import com.BankManagementSystem.BankManagement.dto.account.DashboardResponse;
import com.BankManagementSystem.BankManagement.dto.transaction.TransactionHistoryResponse;
import com.BankManagementSystem.BankManagement.dto.transaction.TransactionRequest;
import com.BankManagementSystem.BankManagement.dto.transaction.TransactionResponse;
import com.BankManagementSystem.BankManagement.dto.transaction.TransferRequest;
import com.BankManagementSystem.BankManagement.entity.Account;
import com.BankManagementSystem.BankManagement.entity.Customer;
import com.BankManagementSystem.BankManagement.entity.Transaction;
import com.BankManagementSystem.BankManagement.enums.AccountStatus;
import com.BankManagementSystem.BankManagement.enums.TransactionType;
import com.BankManagementSystem.BankManagement.exception.ResourceNotFoundException;
import com.BankManagementSystem.BankManagement.repository.AccountRepository;
import com.BankManagementSystem.BankManagement.repository.CustomerRepository;
import com.BankManagementSystem.BankManagement.repository.TransactionRepository;
import com.BankManagementSystem.BankManagement.services.AccountService;
import com.BankManagementSystem.BankManagement.services.PdfStatementService;
import com.BankManagementSystem.BankManagement.util.AccountNumberGenerator;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    private final TransactionRepository transactionRepository;
    private final PdfStatementService pdfStatementService;

   public AccountServiceImpl(AccountRepository accountRepository,
                          CustomerRepository customerRepository,
                          TransactionRepository transactionRepository,
                                PdfStatementService pdfStatementService) {

    this.accountRepository = accountRepository;
    this.customerRepository = customerRepository;
    this.transactionRepository = transactionRepository;
    this.pdfStatementService=pdfStatementService;
}
    @Override
    public AccountResponse createAccount(CreateAccountRequest request, String username) {

        Customer customer = customerRepository.findByUserUsername(username)
        .orElseThrow(() ->
                new ResourceNotFoundException("Customer not found"));

        String accountNumber;

        do {
            accountNumber = AccountNumberGenerator.generate();
        } while (accountRepository.existsByAccountNumber(accountNumber));

        Account account = Account.builder()
                .accountNumber(accountNumber)
                .accountType(request.getAccountType())
                .balance(new BigDecimal(request.getInitialDeposit()))
                .status(AccountStatus.ACTIVE)
                .customer(customer)
                .build();

        account = accountRepository.save(account);

        return mapToResponse(account);
    }

    @Override
    public AccountResponse getAccounts(String accountNumber) {

        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Account not found"));

        return mapToResponse(account);
    }

    @Override
public List<AccountResponse> getAllAccounts(String username) {

    List<Account> accounts = accountRepository.findByCustomerUserUsername(username);

    return accounts.stream()
            .map(account -> AccountResponse.builder()
                    .id(account.getId())
                    .accountNumber(account.getAccountNumber())
                    .accountType(account.getAccountType())
                    .balance(account.getBalance())
                    .status(account.getStatus())
                    .build())
            .toList();
}

    private AccountResponse mapToResponse(Account account) {

        return AccountResponse.builder()
                .id(account.getId())
                .accountNumber(account.getAccountNumber())
                .accountType(account.getAccountType())
                .balance(account.getBalance())
                .status(account.getStatus())
                .build();
    }

           @Transactional
public TransactionResponse deposit(TransactionRequest request, String username) {

    Account account = accountRepository.findByAccountNumber(request.getAccountNumber())
            .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

    if (!account.getCustomer().getUser().getUsername().equals(username)) {
        throw new SecurityException("You are not allowed to access this account");
    }

    account.setBalance(account.getBalance().add(request.getAmount()));
    accountRepository.save(account);

    Transaction transaction = Transaction.builder()
            .transactionType(TransactionType.DEPOSIT)
            .amount(request.getAmount())
            .receiverAccount(account)
            .description("DEPOSIT")
            .build();

    transactionRepository.save(transaction);

    return TransactionResponse.builder()
            .accountNumber(account.getAccountNumber())
            .transactionType("DEPOSIT")
            .amount(request.getAmount())
            .balance(account.getBalance())
            .message("Deposit successful")
            .transactionDate(transaction.getTransactionDate())
            .build();
}


                @Override
        @Transactional
        public TransactionResponse withdraw(TransactionRequest request, String username) {

            Account account = accountRepository.findByAccountNumber(request.getAccountNumber())
                    .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

            // 🔐 OWNERSHIP CHECK
            if (!account.getCustomer().getUser().getUsername().equals(username)) {
                throw new SecurityException("You are not allowed to access this account");
            }

            if (account.getStatus() != AccountStatus.ACTIVE) {
                throw new IllegalStateException("Account is not active.");
            }

            if (request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Amount must be greater than zero.");
            }

            if (account.getBalance().compareTo(request.getAmount()) < 0) {
                throw new IllegalArgumentException("Insufficient balance.");
            }

            account.setBalance(account.getBalance().subtract(request.getAmount()));
            accountRepository.save(account);

            Transaction transaction = Transaction.builder()
                    .transactionType(TransactionType.WITHDRAW)
                    .amount(request.getAmount())
                    .senderAccount(account)
                    .description("WiTHDRAWAL")
                    .build();

            transactionRepository.save(transaction);

            return TransactionResponse.builder()
                    .accountNumber(account.getAccountNumber())
                    .transactionType("WITHDRAW")
                    .amount(request.getAmount())
                    .balance(account.getBalance())
                    .message("Withdraw successful")
                    .transactionDate(transaction.getTransactionDate())
                    .build();
        }


                @Override
                @Transactional
                public TransactionResponse transfer(TransferRequest request, String username) {

                Account sender = accountRepository.findByAccountNumber(request.getFromAccountNumber())
                        .orElseThrow(() -> new ResourceNotFoundException("Sender account not found"));

                Account receiver = accountRepository.findByAccountNumber(request.getToAccountNumber())
                        .orElseThrow(() -> new ResourceNotFoundException("Receiver account not found"));

                //  OWNERSHIP CHECK (IMPORTANT)
                if (!sender.getCustomer().getUser().getUsername().equals(username)) {
                        throw new SecurityException("You are not allowed to transfer from this account");
                }

                if (sender.getAccountNumber().equals(receiver.getAccountNumber())) {
                        throw new IllegalArgumentException("Cannot transfer to same account");
                }

                if (sender.getStatus() != AccountStatus.ACTIVE || receiver.getStatus() != AccountStatus.ACTIVE) {
                        throw new IllegalStateException("One of the accounts is not active");
                }

                if (request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
                        throw new IllegalArgumentException("Amount must be greater than zero");
                }

                if (sender.getBalance().compareTo(request.getAmount()) < 0) {
                        throw new IllegalArgumentException("Insufficient balance");
                }

                sender.setBalance(sender.getBalance().subtract(request.getAmount()));
                receiver.setBalance(receiver.getBalance().add(request.getAmount()));

                accountRepository.save(sender);
                accountRepository.save(receiver);

                Transaction transaction = Transaction.builder()
                        .transactionType(TransactionType.TRANSFER)
                        .amount(request.getAmount())
                        .senderAccount(sender)
                        .receiverAccount(receiver)
                        .description(request.getDescription())
                        .build();

                transactionRepository.save(transaction);

                return TransactionResponse.builder()
                        .accountNumber(sender.getAccountNumber())
                        .transactionType("TRANSFER")
                        .amount(request.getAmount())
                        .balance(sender.getBalance())
                        .message("Transfer successful")
                        .transactionDate(transaction.getTransactionDate())
                        .build();
                }

        @Override
        public Page<TransactionHistoryResponse> getTransactionHistory(
        String accountNumber,
        String username, int page,
        int size) {

    // Find the account
    Account account = accountRepository.findByAccountNumber(accountNumber)
            .orElseThrow(() ->
                    new ResourceNotFoundException("Account not found"));

    // Verify the logged-in user owns this account
    if (!account.getCustomer().getUser().getUsername().equals(username)) {
        throw new SecurityException("You are not allowed to view this account.");
    }

    // Fetch transaction history
    Pageable pageable = PageRequest.of(page, size);

        Page<Transaction> transactions =
        transactionRepository.findTransactionHistory(
                accountNumber,
                pageable);

    // Convert Entity -> DTO
    return transactions.map(transaction ->
        TransactionHistoryResponse.builder()
                .transactionId(transaction.getId())
                .transactionType(transaction.getTransactionType())
                .amount(transaction.getAmount())
                .senderAccount(
                        transaction.getSenderAccount() != null
                                ? transaction.getSenderAccount().getAccountNumber()
                                : null)
                .receiverAccount(
                        transaction.getReceiverAccount() != null
                                ? transaction.getReceiverAccount().getAccountNumber()
                                : null)
                .description(transaction.getDescription())
                .transactionDate(transaction.getTransactionDate())
                .build());
}

@Override
public Page<TransactionHistoryResponse> getTransactionHistory(
        String accountNumber,
        String username,
        TransactionType type,
        int page,
        int size) {

    Account account = accountRepository.findByAccountNumber(accountNumber)
            .orElseThrow(() ->
                    new ResourceNotFoundException("Account not found"));

    if (!account.getCustomer().getUser().getUsername().equals(username)) {
        throw new SecurityException("You are not allowed to view this account.");
    }

    Pageable pageable = PageRequest.of(page, size);

    Page<Transaction> transactions;

    if (type == null) {
        transactions = transactionRepository.findTransactionHistory(
                accountNumber,
                pageable);
    } else {
        transactions = transactionRepository.findTransactionHistoryByType(
                accountNumber,
                type,
                pageable);
    }

    return transactions.map(transaction ->
            TransactionHistoryResponse.builder()
                    .transactionId(transaction.getId())
                    .transactionType(transaction.getTransactionType())
                    .amount(transaction.getAmount())
                    .senderAccount(
                            transaction.getSenderAccount() != null
                                    ? transaction.getSenderAccount().getAccountNumber()
                                    : null)
                    .receiverAccount(
                            transaction.getReceiverAccount() != null
                                    ? transaction.getReceiverAccount().getAccountNumber()
                                    : null)
                    .description(transaction.getDescription())
                    .transactionDate(transaction.getTransactionDate())
                    .build());
}

        @Override
        public List<TransactionHistoryResponse> getMiniStatement(
                String accountNumber,
                String username) {

    Account account = accountRepository.findByAccountNumber(accountNumber)
            .orElseThrow(() ->
                    new ResourceNotFoundException("Account not found"));

    if (!account.getCustomer().getUser().getUsername().equals(username)) {
        throw new SecurityException("You are not allowed to view this account.");
    }

    Pageable pageable = PageRequest.of(0, 5);

    List<Transaction> transactions =
            transactionRepository.findTop5Transactions(
                    accountNumber,
                    pageable);

    return transactions.stream()
            .map(transaction -> TransactionHistoryResponse.builder()
                    .transactionId(transaction.getId())
                    .transactionType(transaction.getTransactionType())
                    .amount(transaction.getAmount())
                    .senderAccount(
                            transaction.getSenderAccount() != null
                                    ? transaction.getSenderAccount().getAccountNumber()
                                    : null)
                    .receiverAccount(
                            transaction.getReceiverAccount() != null
                                    ? transaction.getReceiverAccount().getAccountNumber()
                                    : null)
                    .description(transaction.getDescription())
                    .transactionDate(transaction.getTransactionDate())
                    .build())
            .toList();
}

                @Override
                public ByteArrayInputStream downloadStatement(
                        String accountNumber,
                        LocalDate from,
                        LocalDate to,
                        String username) {

                Account account = accountRepository.findByAccountNumber(accountNumber)
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Account not found"));

                if (!account.getCustomer().getUser().getUsername().equals(username)) {
                        throw new RuntimeException("Access denied");
                }

                List<Transaction> transactions =
                        transactionRepository.findStatementTransactions(
                                accountNumber,
                                from.atStartOfDay(),
                                to.atTime(23, 59, 59));

                return pdfStatementService.generateStatement(
                        account,
                        transactions);
                }

                @Override
                public DashboardResponse getDashboard(
                        String accountNumber,
                        String username) {

                Account account = accountRepository.findByAccountNumber(accountNumber)
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Account not found"));

                if (!account.getCustomer().getUser().getUsername().equals(username)) {
                        throw new RuntimeException("Access Denied");
                }

                List<TransactionHistoryResponse> recentTransactions =
                        transactionRepository.findTop5Transactions(
                                accountNumber,
                                PageRequest.of(0, 5))
                        .stream()
                        .map(transaction -> TransactionHistoryResponse.builder()
                                .transactionId(transaction.getId())
                                .transactionType(transaction.getTransactionType())
                                .amount(transaction.getAmount())
                                .senderAccount(
                                        transaction.getSenderAccount() != null
                                                ? transaction.getSenderAccount().getAccountNumber()
                                                : null)
                                .receiverAccount(
                                        transaction.getReceiverAccount() != null
                                                ? transaction.getReceiverAccount().getAccountNumber()
                                                : null)
                                .description(transaction.getDescription())
                                .transactionDate(transaction.getTransactionDate())
                                .build())
                        .toList();

    return DashboardResponse.builder()
            .customerName(account.getCustomer().getFirstName()+ " "+ account.getCustomer().getLastName())
            .accountNumber(account.getAccountNumber())
            .accountType(account.getAccountType())
            .currentBalance(account.getBalance())
            .recentTransactions(recentTransactions)
            .build();
}
}