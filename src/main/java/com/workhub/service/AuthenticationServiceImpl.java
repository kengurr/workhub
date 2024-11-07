package com.workhub.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.workhub.dto.AuthenticationRequest;
import com.workhub.dto.AuthenticationResponse;
import com.workhub.dto.RegisterRequest;
import com.workhub.entity.Employee;
import com.workhub.entity.Token;
import com.workhub.dto.TokenType;
import com.workhub.repository.EmployeeRepository;
import com.workhub.repository.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final EmployeeRepository repository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthenticationResponse register(RegisterRequest request) {
        var employee = Employee.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();
        var savedEmployee = repository.save(employee);
        var jwtToken = jwtService.generateToken(employee);
        var refreshToken = jwtService.generateRefreshToken(employee);
        saveUserToken(savedEmployee, jwtToken);
        return AuthenticationResponse.builder().accessToken(jwtToken).refreshToken(refreshToken).build();
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        var employee = repository.findByEmail(request.getEmail()).orElseThrow();
        var jwtToken = jwtService.generateToken(employee);
        var refreshToken = jwtService.generateRefreshToken(employee);
        revokeAllUserTokens(employee);
        saveUserToken(employee, jwtToken);
        return AuthenticationResponse.builder().accessToken(jwtToken).refreshToken(refreshToken).build();
    }

    @Override
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {

        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String employeeEmail;
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        employeeEmail = jwtService.extractUsername(refreshToken);
        if (employeeEmail != null) {
            var employee = this.repository.findByEmail(employeeEmail).orElseThrow();
            if (jwtService.isTokenValid(refreshToken, employee)) {
                var accessToken = jwtService.generateToken(employee);
                revokeAllUserTokens(employee);
                saveUserToken(employee, accessToken);

                AuthenticationResponse authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }

    private void saveUserToken(Employee employee, String jwtToken) {
        var token = Token.builder()
                .employee(employee)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(Employee employee) {
        var validUserTokens = tokenRepository.findAllValidTokenByEmployee(employee.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

}
