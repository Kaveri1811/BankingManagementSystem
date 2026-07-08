package com.BankManagementSystem.BankManagement.services;

import com.BankManagementSystem.BankManagement.dto.request.LoginRequest;
import com.BankManagementSystem.BankManagement.dto.request.RegisterRequest;
import com.BankManagementSystem.BankManagement.dto.response.AuthResponse;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

}