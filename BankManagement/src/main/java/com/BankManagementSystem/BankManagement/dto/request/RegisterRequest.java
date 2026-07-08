package com.BankManagementSystem.BankManagement.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {

    private String username;

    private String email;

    private String password;

    private String firstName;

    private String lastName;

    private String phone;

    private String address;

    private LocalDate dateOfBirth;
}