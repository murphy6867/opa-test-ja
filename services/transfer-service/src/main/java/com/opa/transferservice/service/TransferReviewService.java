package com.opa.transferservice.service;

import com.opa.transferservice.client.AuthClient;
import com.opa.transferservice.client.OpaClient;
import com.opa.transferservice.client.model.AccountResponse;
import com.opa.transferservice.dto.opa.OpaDecision;
import com.opa.transferservice.dto.opa.OpaError;
import com.opa.transferservice.dto.review.TransferReviewRequest;
import com.opa.transferservice.dto.review.TransferReviewResponse;
import com.opa.transferservice.security.ReviewTokenService;
import com.opa.transferservice.security.model.ReviewSnapshot;
import com.opa.transferservice.util.PolicyDeniedException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TransferReviewService {

    private final OpaClient opaClient;
    private final AuthClient authClient;
    private final ReviewTokenService jwtService;

    @Value("${transfer.fee.rate:0.01}")
    private BigDecimal feeRate;

    public TransferReviewResponse reviewTransferService(
            TransferReviewRequest request
    ) {

        String role = authClient.getRoleByUserId(request.getUserId());

        OpaDecision decision = opaClient.checkTransfer(role, request.getAmount().doubleValue());

        if (decision == null) {
            throw new PolicyDeniedException("OPA_ERROR", "OPA returned null decision", 502);
        }

        if (!decision.isAllow()) {
            OpaError err = decision.getError();
            String code = (err != null && err.getCode() != null && !err.getCode().isBlank())
                ? err.getCode()
                : "TRANSFER_DENIED";
            String msg = (err != null && err.getMessage() != null && !err.getMessage().isBlank())
                ? err.getMessage()
                : "Transfer not allowed";
            int status = (err != null && err.getHttpStatus() > 0) ? err.getHttpStatus() : 403;

            throw new PolicyDeniedException(code, msg, status);
        }

        List<AccountResponse> accounts = authClient.getAccountsByNumbers(
            List.of(request.getFromAccount(), request.getToAccount())
        );

        AccountResponse fromAccount = !accounts.isEmpty() ? accounts.get(0) : null;
        AccountResponse toAccount = accounts.size() > 1 ? accounts.get(1) : null;

        if (fromAccount == null) {
            throw new PolicyDeniedException("FROM_ACCOUNT_NOT_FOUND", "From account not found", 404);
        }

        BigDecimal fromBalance = fromAccount.getBalance();
        if (fromBalance == null) {
            throw new PolicyDeniedException("BALANCE_UNAVAILABLE", "From account balance unavailable", 502);
        }

        if (fromBalance.compareTo(request.getAmount()) < 0) {
            throw new PolicyDeniedException("INSUFFICIENT_BALANCE", "From account does not have enough balance", 400);
        }

        String fromAccountName = (fromAccount.getAccountName() != null && !fromAccount.getAccountName().isBlank())
                ? fromAccount.getAccountName()
                : request.getFromAccount();

        String toAccountName = (toAccount != null && toAccount.getAccountName() != null && !toAccount.getAccountName().isBlank())
                ? toAccount.getAccountName()
                : request.getToAccount();

        String toBankCode = (toAccount != null && toAccount.getBankCode() != null && !toAccount.getBankCode().isBlank())
                ? toAccount.getBankCode()
                : request.getBankCode();


        BigDecimal multiplier = decision.getFeeMultiplier();
        if (multiplier == null) {
            multiplier = BigDecimal.ONE;
        }

        BigDecimal fee = request.getAmount()
                .multiply(feeRate)
                .multiply(multiplier)
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal totalAmount = request.getAmount().add(fee);

        boolean approvalRequired = decision.getApprovalRequired() != null && decision.getApprovalRequired();

        ReviewSnapshot snapshot = new ReviewSnapshot(
                request.getFromAccount(),
                request.getToAccount(),
                toBankCode,
                request.getAmount(),
                request.getNote(),
                fee,
                totalAmount,
                role,
                approvalRequired
        );

        String token = jwtService.generateToken(request.getUserId(), snapshot);

        return TransferReviewResponse.builder()
                .fromAccountName(fromAccountName)
                .toAccountName(toAccountName)
                .toBankCode(toBankCode)
                .amount(request.getAmount())
                .fee(fee)
                .totalAmount(totalAmount)
                .exchangeRate(null)
                .approvalRequired(approvalRequired)
                .reviewToken(token)
                .build();
    }
}