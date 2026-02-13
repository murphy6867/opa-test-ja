package com.ops.authservice.service;

import com.ops.authservice.client.OpaClient;
import com.ops.authservice.dto.OpaDecision;
import com.ops.authservice.model.User;
import com.ops.authservice.repo.MockUserRepository;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AuthService {

    private final OpaClient opaClient;
    private final MockUserRepository repository;

    public AuthService(OpaClient opaClient, MockUserRepository repository) {
        this.opaClient = opaClient;
        this.repository = repository;
    }

    public User login(String username) {

        User user = repository.findByUsername(username);

        if (user == null) {
            throw new RuntimeException("User not found");
        }

        return user;
    }

    public User getUserBalance(String username) {
        User user = repository.findByUsername(username);

        if (user == null) {
            throw new RuntimeException("User not found");
        }

        return repository.findByUsername(username);
    }

}
