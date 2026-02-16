package com.ops.authservice.repository;

import com.ops.authservice.entity.Account;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    boolean existsByAccountNumber(String accountNumber);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Account> findByAccountNumber(String accountNumber);

    List<Account> findByAccountNumberIn(List<String> accountNumbers);

    List<Account> findByUserUsername(String username);
}
