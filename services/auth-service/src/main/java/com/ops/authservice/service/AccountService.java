package com.ops.authservice.service;

import com.ops.authservice.dto.AccountResponse;
import com.ops.authservice.dto.CreateAccountRequest;
import com.ops.authservice.entity.Account;
import com.ops.authservice.entity.User;
import com.ops.authservice.repository.AccountRepository;
import com.ops.authservice.repository.UserRepository;
import com.ops.authservice.util.AccountNotFoundException;
import com.ops.authservice.util.UserNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AccountService {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public AccountResponse createAccount(CreateAccountRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UserNotFoundException(request.getUsername()));

        Account account = Account.builder()
                .user(user)
                .accountName(request.getAccountName())
                .accountNumber(generateUniqueAccountNumber())
                .bankCode(request.getBankCode())
                .balance(normalizeMoney(request.getInitialBalance()))
                .build();

        Account saved = accountRepository.save(account);
        return toResponse(saved);
    }

    public List<AccountResponse> getAccountsByUsername(String username) {
        if (userRepository.findByUsername(username).isEmpty()) {
            throw new UserNotFoundException(username);
        }

        return accountRepository.findByUserUsername(username)
                .stream()
                .map(AccountService::toResponse)
                .toList();
    }

    public AccountResponse getByAccountNumber(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException(accountNumber));
        return toResponse(account);
    }

    private static AccountResponse toResponse(Account account) {
        return AccountResponse.builder()
                .id(account.getId())
                .userId(account.getUser().getId())
                .username(account.getUser().getUsername())
                .accountName(account.getAccountName())
                .accountNumber(account.getAccountNumber())
                .bankCode(account.getBankCode() == null ? null : account.getBankCode().name())
                .balance(account.getBalance())
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
}
