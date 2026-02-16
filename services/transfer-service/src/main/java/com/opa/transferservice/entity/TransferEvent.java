package com.opa.transferservice.entity;

import com.opa.transferservice.enums.TransferEventType;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "transfer_events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransferEvent {
    @Id
    @GeneratedValue
    private UUID id;

    private UUID transferId;

    @Enumerated(EnumType.STRING)
    private TransferEventType eventType;

    private String detail;     // optional reason / message
    private String actor;      // SYSTEM / USER / APPROVER

    private Instant createdAt;
}
