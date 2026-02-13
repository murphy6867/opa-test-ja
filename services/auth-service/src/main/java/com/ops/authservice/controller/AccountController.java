package com.ops.authservice.controller;

import com.ops.authservice.dto.AccountResponse;
import com.ops.authservice.dto.ApiResponse;
import com.ops.authservice.dto.CreateAccountRequest;
import com.ops.authservice.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/v1/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<ApiResponse<AccountResponse>> createAccount(
            @Valid @RequestBody CreateAccountRequest request) {

        AccountResponse response = accountService.createAccount(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<AccountResponse>builder()
                        .success(true)
                        .message("Account created")
                        .data(response)
                        .timestamp(Instant.now())
                        .build());
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<ApiResponse<List<AccountResponse>>> getByUsername(
            @PathVariable String username) {

        List<AccountResponse> accounts = accountService.getAccountsByUsername(username);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.<List<AccountResponse>>builder()
                        .success(true)
                        .message("Accounts fetched")
                        .data(accounts)
                        .timestamp(Instant.now())
                        .build());
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<ApiResponse<AccountResponse>> getByAccountNumber(
            @PathVariable String accountNumber) {

        AccountResponse account = accountService.getByAccountNumber(accountNumber);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.<AccountResponse>builder()
                        .success(true)
                        .message("Account fetched")
                        .data(account)
                        .timestamp(Instant.now())
                        .build());
    }
}
