package com.opa.transferservice.repo;

import com.opa.transferservice.entity.TransferEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TransferEventRepository extends JpaRepository<TransferEvent, UUID> {
}
