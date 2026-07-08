package com.BankManagementSystem.BankManagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.BankManagementSystem.BankManagement.entity.Beneficiary;

public interface BeneficiaryRepository extends JpaRepository<Beneficiary, Long> {
}