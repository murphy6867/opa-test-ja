package com.opa.transferservice.service;

import com.opa.transferservice.dto.submit.TransferSubmitRequest;
import com.opa.transferservice.dto.submit.TransferSubmitResponse;
import com.opa.transferservice.entity.TransferEvent;
import com.opa.transferservice.entity.Transfers;
import com.opa.transferservice.enums.TransferEventType;
import com.opa.transferservice.client.AuthClient;
import com.opa.transferservice.repo.TransferEventRepository;
import com.opa.transferservice.repo.TransfersRepository;
import com.opa.transferservice.security.ReviewTokenService;
import com.opa.transferservice.security.model.ReviewSnapshot;
import com.opa.transferservice.security.model.TokenPayload;
import com.opa.transferservice.util.PolicyDeniedException;
import lombok.RequiredArgsConstructor;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class TransferSubmitService {

    private final ReviewTokenService reviewTokenService;
    private final AuthClient authClient;
    private final TransfersRepository transfersRepository;
    private final TransferEventRepository transferEventRepository;

    @Transactional
    public TransferSubmitResponse submitTransferService(TransferSubmitRequest request) {
        if (request == null) {
            throw new PolicyDeniedException("INVALID_REQUEST", "Request body is required", 400);
        }
        if (request.getReviewToken() == null || request.getReviewToken().isBlank()) {
            throw new PolicyDeniedException("INVALID_REVIEW_TOKEN", "reviewToken is required", 400);
        }
        if (request.getOtpCode() == null || request.getOtpCode().isBlank()) {
            throw new PolicyDeniedException("INVALID_OTP", "otpCode is required", 400);
        }

        TokenPayload payload;
        try {
            payload = reviewTokenService.verifyAndConsume(request.getReviewToken());
        } catch (RuntimeException ex) {
            throw new PolicyDeniedException("INVALID_REVIEW_TOKEN", ex.getMessage(), 400);
        }

        ReviewSnapshot snapshot = payload.getSnapshot();
        if (snapshot == null) {
            throw new PolicyDeniedException("INVALID_REVIEW_TOKEN", "Snapshot missing in token", 400);
        }

        if (payload.getUserId() == null || payload.getUserId().isBlank()) {
            throw new PolicyDeniedException("INVALID_REVIEW_TOKEN", "UserId missing in token", 400);
        }

        if (snapshot.getFromAccount() == null || snapshot.getFromAccount().isBlank()) {
            throw new PolicyDeniedException("INVALID_REVIEW_TOKEN", "fromAccount missing in token", 400);
        }
        if (snapshot.getToAccount() == null || snapshot.getToAccount().isBlank()) {
            throw new PolicyDeniedException("INVALID_REVIEW_TOKEN", "toAccount missing in token", 400);
        }
        if (snapshot.getBankCode() == null || snapshot.getBankCode().isBlank()) {
            throw new PolicyDeniedException("INVALID_REVIEW_TOKEN", "bankCode missing in token", 400);
        }
        if (snapshot.getAmount() == null || snapshot.getFee() == null || snapshot.getTotalAmount() == null) {
            throw new PolicyDeniedException("INVALID_REVIEW_TOKEN", "amount/fee/totalAmount missing in token", 400);
        }

        boolean approvalRequired = snapshot.getApprovalRequired() != null && snapshot.getApprovalRequired();
        TransferEventType status = approvalRequired
                ? TransferEventType.WAITING_APPROVAL
                : TransferEventType.SUBMITTED;

        String idempotencyKey = payload.getJti();
        if (idempotencyKey == null || idempotencyKey.isBlank()) {
            throw new PolicyDeniedException("INVALID_REVIEW_TOKEN", "jti missing in token", 400);
        }

        try {
            authClient.debitAccount(snapshot.getFromAccount(), snapshot.getTotalAmount(), idempotencyKey);
        } catch (RuntimeException ex) {
            throw new PolicyDeniedException("ACCOUNT_DEBIT_FAILED", ex.getMessage(), 502);
        }

        Instant now = Instant.now();

        Transfers transfer = Transfers.builder()
                .fromAccount(snapshot.getFromAccount())
                .toAccount(snapshot.getToAccount())
                .toBankCode(snapshot.getBankCode())
                .amount(snapshot.getAmount())
                .fee(snapshot.getFee())
                .totalAmount(snapshot.getTotalAmount())
                .exchangeRate(null)
                .status(status)
                .approvalRequired(approvalRequired)
                .idempotencyKey(idempotencyKey)
                .createdBy(payload.getUserId())
                .createdAt(now)
                .updatedAt(now)
                .build();

        Transfers savedTransfer = transfersRepository.save(transfer);

        transferEventRepository.save(TransferEvent.builder()
                .transferId(savedTransfer.getId())
                .eventType(TransferEventType.DEBIT_SUCCESS)
                .detail("Debited totalAmount")
                .actor("SYSTEM")
                .createdAt(now)
                .build());

        transferEventRepository.save(TransferEvent.builder()
                .transferId(savedTransfer.getId())
                .eventType(status)
                .detail(approvalRequired ? "Waiting for approval" : "Submitted")
                .actor("USER")
                .createdAt(now)
                .build());

        return TransferSubmitResponse.builder()
            .transactionId(savedTransfer.getId() == null ? null : savedTransfer.getId().toString())
            .status(status)
                .submittedAt(now)
                .build();
    }
}
