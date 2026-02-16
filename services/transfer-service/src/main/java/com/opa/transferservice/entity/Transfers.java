package com.opa.transferservice.entity;

import com.opa.transferservice.enums.TransferEventType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "transfers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transfers {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "from_account", nullable = false, length = 10)
    private String fromAccount;

    @Column(name = "to_account", nullable = false, length = 10)
    private String toAccount;

    @Column(name = "to_bank_code", nullable = false)
    private String toBankCode;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "fee", nullable = false)
    private BigDecimal fee;

    @Column(name = "total_amount", nullable = false)
    private BigDecimal totalAmount;

    @Column(name = "exchange_rate", nullable = true)
    private BigDecimal exchangeRate;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private TransferEventType status;

    @Column(name = "approval_required", nullable = false)
    private Boolean approvalRequired;

    @Column(unique = true)
    private String idempotencyKey;

    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;
}
