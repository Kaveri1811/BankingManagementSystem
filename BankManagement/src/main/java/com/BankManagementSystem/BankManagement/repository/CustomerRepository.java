package com.BankManagementSystem.BankManagement.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.BankManagementSystem.BankManagement.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

     Optional<Customer> findByUserUsername(String username);
     
}