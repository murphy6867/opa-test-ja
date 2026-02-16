package com.ops.authservice.service;

import com.ops.authservice.dto.account.AccountResponse;
import com.ops.authservice.dto.account.CreateAccountRequest;
import com.ops.authservice.dto.account.DebitAccountRequest;
import com.ops.authservice.dto.account.DebitAccountResponse;
import com.ops.authservice.entity.Account;
import com.ops.authservice.entity.Bank;
import com.ops.authservice.entity.User;
import com.ops.authservice.repository.AccountRepository;
import com.ops.authservice.repository.BankRepository;
import com.ops.authservice.repository.UserRepository;
import com.ops.authservice.util.AccountNotFoundException;
import com.ops.authservice.util.UserNotFoundException;
import com.ops.authservice.util.BankNotFoundException;
import com.ops.authservice.util.InsufficientBalanceException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class AccountService {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final BankRepository bankRepository;

    public AccountResponse createAccount(CreateAccountRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UserNotFoundException(request.getUsername()));

        Bank bank = bankRepository.findById(request.getBankCode())
            .orElseThrow(() -> new BankNotFoundException(request.getBankCode()));

        Account account = Account.builder()
                .user(user)
                .accountName(request.getAccountName())
                .accountNumber(generateUniqueAccountNumber())
            .bank(bank)
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

    public List<AccountResponse> getByAccountNumbers(List<String> accountNumbers) {
        if (accountNumbers == null || accountNumbers.isEmpty()) {
            return List.of();
        }

        List<Account> accounts = accountRepository.findByAccountNumberIn(accountNumbers);
        Map<String, AccountResponse> byNumber = new HashMap<>();
        for (Account account : accounts) {
            if (account.getAccountNumber() != null) {
                byNumber.put(account.getAccountNumber(), toResponse(account));
            }
        }

        for (String accountNumber : accountNumbers) {
            if (!byNumber.containsKey(accountNumber)) {
                throw new AccountNotFoundException(accountNumber);
            }
        }

        return accountNumbers.stream()
                .map(byNumber::get)
                .toList();
    }

    public DebitAccountResponse debit(DebitAccountRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Request body is required");
        }
        if (request.getAccountNumber() == null || request.getAccountNumber().isBlank()) {
            throw new IllegalArgumentException("accountNumber is required");
        }
        if (request.getAmount() == null) {
            throw new IllegalArgumentException("amount is required");
        }

        BigDecimal amount = normalizeMoney(request.getAmount());
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("amount must be positive");
        }

        Account account = accountRepository.findByAccountNumber(request.getAccountNumber())
                .orElseThrow(() -> new AccountNotFoundException(request.getAccountNumber()));

        BigDecimal balance = account.getBalance();
        if (balance == null) {
            throw new IllegalArgumentException("balance unavailable");
        }

        if (balance.compareTo(amount) < 0) {
            throw new InsufficientBalanceException("Insufficient balance");
        }

        BigDecimal newBalance = balance.subtract(amount).setScale(2, RoundingMode.HALF_UP);
        account.setBalance(newBalance);
        accountRepository.save(account);

        return DebitAccountResponse.builder()
                .accountNumber(account.getAccountNumber())
                .debitedAmount(amount)
                .newBalance(newBalance)
                .build();
    }

    private static AccountResponse toResponse(Account account) {
        return AccountResponse.builder()
                .id(account.getId())
                .userId(account.getUser().getId())
                .username(account.getUser().getUsername())
                .accountName(account.getAccountName())
                .accountNumber(account.getAccountNumber())
                .bankCode(account.getBank() == null ? null : account.getBank().getCode())
                .bankName(account.getBank() == null ? null : account.getBank().getName())
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
