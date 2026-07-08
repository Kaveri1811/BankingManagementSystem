package com.BankManagementSystem.BankManagement.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.BankManagementSystem.BankManagement.entity.Transaction;
import com.BankManagementSystem.BankManagement.enums.TransactionType;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

         @Query("""
      SELECT t
      FROM Transaction t
      LEFT JOIN t.senderAccount s
      LEFT JOIN t.receiverAccount r
      WHERE s.accountNumber = :accountNumber
         OR r.accountNumber = :accountNumber
      ORDER BY t.transactionDate DESC
      """)
   Page<Transaction> findTransactionHistory(
        @Param("accountNumber") String accountNumber,
        Pageable pageable);



               @Query("""
         SELECT t
         FROM Transaction t
         LEFT JOIN t.senderAccount s
         LEFT JOIN t.receiverAccount r
         WHERE (s.accountNumber = :accountNumber
            OR r.accountNumber = :accountNumber)
         AND t.transactionType = :type
         ORDER BY t.transactionDate DESC
         """)
         Page<Transaction> findTransactionHistoryByType(
        @Param("accountNumber") String accountNumber,
        @Param("type") TransactionType type,
        Pageable pageable);



        @Query("""
      SELECT t
      FROM Transaction t
      LEFT JOIN t.senderAccount s
      LEFT JOIN t.receiverAccount r
      WHERE s.accountNumber = :accountNumber
         OR r.accountNumber = :accountNumber
      ORDER BY t.transactionDate DESC
      """)
      List<Transaction> findTop5Transactions(
            @Param("accountNumber") String accountNumber,
            Pageable pageable);

         @Query("""
         SELECT t
         FROM Transaction t
         LEFT JOIN t.senderAccount s
         LEFT JOIN t.receiverAccount r
         WHERE (s.accountNumber = :accountNumber
            OR r.accountNumber = :accountNumber)
         AND t.transactionDate BETWEEN :from AND :to
         ORDER BY t.transactionDate ASC
         """)
         List<Transaction> findStatementTransactions(
               @Param("accountNumber") String accountNumber,
               @Param("from") LocalDateTime from,
               @Param("to") LocalDateTime to);
}