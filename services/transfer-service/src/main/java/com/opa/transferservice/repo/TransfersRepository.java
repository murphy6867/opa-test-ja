package com.opa.transferservice.repo;

import com.opa.transferservice.entity.Transfers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TransfersRepository extends JpaRepository<Transfers, UUID> {
    Optional<Transfers> findByIdempotencyKey(String idempotencyKey);
}
