package com.BankManagementSystem.BankManagement.services.serviceImpl;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.BankManagementSystem.BankManagement.dto.request.LoginRequest;
import com.BankManagementSystem.BankManagement.dto.request.RegisterRequest;
import com.BankManagementSystem.BankManagement.dto.response.AuthResponse;
import com.BankManagementSystem.BankManagement.entity.Customer;
import com.BankManagementSystem.BankManagement.entity.User;
import com.BankManagementSystem.BankManagement.enums.Role;
import com.BankManagementSystem.BankManagement.exception.ResourceAlreadyExistsException;
import com.BankManagementSystem.BankManagement.repository.CustomerRepository;
import com.BankManagementSystem.BankManagement.repository.UserRepository;
import com.BankManagementSystem.BankManagement.security.JwtService;
import com.BankManagementSystem.BankManagement.services.AuthService;




@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthServiceImpl(UserRepository userRepository,
                       CustomerRepository customerRepository,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager,
                       JwtService jwtService) {

    this.userRepository = userRepository;
    this.customerRepository = customerRepository;
    this.passwordEncoder = passwordEncoder;
    this.authenticationManager = authenticationManager;
    this.jwtService = jwtService;
}

    @Override
    public AuthResponse register(RegisterRequest request) {

        // Check username
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new ResourceAlreadyExistsException("Username already exists");
        }

        // Check email
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResourceAlreadyExistsException("Email already exists");
        }

        // Create User
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.CUSTOMER)
                .build();

        user = userRepository.save(user);

        // Create Customer
        Customer customer = Customer.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phone(request.getPhone())
                .address(request.getAddress())
                .dateOfBirth(request.getDateOfBirth())
                .user(user)
                .build();

        customerRepository.save(customer);

        return AuthResponse.builder()
                .username(user.getUsername())
                .message("Customer registered successfully.")
                .build();
    }

        @Override
        public AuthResponse login(LoginRequest request) {
            authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                    request.getUsername(),
                    request.getPassword()
            )
        );

        User user = userRepository.findByUsername(request.getUsername())
            .orElseThrow(() ->
                    new UsernameNotFoundException("User not found"));

    String token = jwtService.generateToken(
            user.getUsername(),
            user.getRole().name()
    );

    return AuthResponse.builder()
            .username(user.getUsername())
            .token(token)
            .message("Login Successful")
            .build();
}
}