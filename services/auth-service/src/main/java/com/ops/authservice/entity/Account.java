package com.ops.authservice.entity;

import com.ops.authservice.enums.BankCode;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(
        name = "accounts",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_accounts_account_number", columnNames = "account_number")
        },
        indexes = {
                @Index(name = "idx_accounts_user_id", columnList = "user_id")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "account_name", nullable = false)
    private String accountName;

    @Column(name = "account_number", nullable = false, length = 10)
    private String accountNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "bank_code", nullable = false, length = 10)
    private BankCode bankCode;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal balance;
}
