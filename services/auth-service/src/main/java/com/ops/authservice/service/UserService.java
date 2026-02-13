package com.ops.authservice.service;

import com.ops.authservice.client.OpaClient;
import com.ops.authservice.dto.CreateUserRequest;
import com.ops.authservice.dto.SignInRequest;
import com.ops.authservice.dto.UserResponse;
import com.ops.authservice.entity.Account;
import com.ops.authservice.entity.User;
import com.ops.authservice.repository.AccountRepository;
import com.ops.authservice.repository.UserRepository;
import com.ops.authservice.util.UserNotFoundException;
import com.ops.authservice.util.UsernameAlreadyExistsException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private final OpaClient opaClient;
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;

    public UserResponse createUser(CreateUserRequest request) {

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UsernameAlreadyExistsException(request.getUsername());
        }

        User user = User.builder()
                .username(request.getUsername())
                .password(request.getPassword())
                .role(request.getRole())
                .build();

        User savedUser = userRepository.save(user);

        Account account = Account.builder()
            .user(savedUser)
            .accountName(request.getAccountName())
            .accountNumber(generateUniqueAccountNumber())
            .bankCode(request.getBankCode())
            .balance(normalizeMoney(request.getBalance()))
            .build();

        Account savedAccount = accountRepository.save(account);

        return UserResponse.builder()
            .id(savedUser.getId())
            .username(savedUser.getUsername())
            .balance(savedAccount.getBalance())
            .accountName(savedAccount.getAccountName())
            .accountNumber(savedAccount.getAccountNumber())
            .role(savedUser.getRole())
            .bankCode(savedAccount.getBankCode() == null ? null : savedAccount.getBankCode().name())
                .build();

    }

    private static BigDecimal normalizeMoney(BigDecimal amount) {
        if (amount == null) {
            return null;
        }
        return amount.setScale(2, RoundingMode.HALF_UP);
    }

    private String generateUniqueAccountNumber() {
        for (int attempt = 0; attempt < 20; attempt++) {
            String candidate = generate10DigitNumber();
            if (!accountRepository.existsByAccountNumber(candidate)) {
                return candidate;
            }
        }
        throw new IllegalStateException("Unable to generate unique account number");
    }

    private static String generate10DigitNumber() {
        StringBuilder sb = new StringBuilder(10);
        for (int i = 0; i < 10; i++) {
            sb.append(SECURE_RANDOM.nextInt(10));
        }
        return sb.toString();
    }

    public UserResponse signInService(SignInRequest request) {

        if (userRepository.findByUsername(request.getUsername()).isEmpty()) {
            throw new UserNotFoundException(request.getUsername());
        }

        User user = userRepository.findByUsername(request.getUsername()).get();

        Account defaultAccount = user.getAccounts().isEmpty() ? null : user.getAccounts().get(0);

        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
            .balance(defaultAccount == null ? null : defaultAccount.getBalance())
            .accountName(defaultAccount == null ? null : defaultAccount.getAccountName())
            .accountNumber(defaultAccount == null ? null : defaultAccount.getAccountNumber())
                .role(user.getRole())
            .bankCode(defaultAccount == null || defaultAccount.getBankCode() == null ? null : defaultAccount.getBankCode().name())
                .build();
    }

}
