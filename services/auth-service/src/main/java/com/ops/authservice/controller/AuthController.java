package com.ops.authservice.controller;

import com.ops.authservice.dto.LoginRequest;
import com.ops.authservice.model.User;
import com.ops.authservice.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/v1/auth")
public class AuthController {

    private final AuthService loginService;
    private final AuthService authService;

    public AuthController(AuthService loginService, AuthService authService) {
        this.loginService = loginService;
        this.authService = authService;
    }

    @GetMapping("/health")
    public String healthCheck() {
        return "OK";
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequest request) {
        User user = loginService.login(request.getUsername());

        Map<String, Object> response = Map.of(
                "username", user.getUsername(),
                "role", user.getRole()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/get-balance/{username}")
    public ResponseEntity<?> getBalanceByUsername(@PathVariable String username) {

        return ResponseEntity.ok(authService.getUserBalance(username));
    }

}