package com.ops.authservice.controller;

import com.ops.authservice.dto.ApiResponse;
import com.ops.authservice.dto.CreateUserRequest;
import com.ops.authservice.dto.SignInRequest;
import com.ops.authservice.dto.UserResponse;
import com.ops.authservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @GetMapping("/health")
    public String healthCheck() {
        return "OK";
    }

    @PostMapping("/sign-up")
    public ResponseEntity<ApiResponse<UserResponse>> createUser(
            @Valid @RequestBody CreateUserRequest request) {

        UserResponse response = userService.createUser(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<UserResponse>builder()
                        .success(true)
                        .message("User created")
                        .data(response)
                        .timestamp(Instant.now())
                        .build());
    }

    @PostMapping("/sign-in")
    public ResponseEntity<ApiResponse<UserResponse>> signIn(
            @Valid @RequestBody SignInRequest request){

        UserResponse response = userService.signInService(request);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.<UserResponse>builder()
                        .success(true)
                        .message("User Signed In")
                        .data(response)
                        .timestamp(Instant.now())
                        .build());
    }
}