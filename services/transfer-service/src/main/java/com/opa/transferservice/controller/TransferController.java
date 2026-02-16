package com.opa.transferservice.controller;

import com.opa.transferservice.dto.ApiResponse;
import com.opa.transferservice.dto.review.TransferReviewRequest;
import com.opa.transferservice.dto.review.TransferReviewResponse;
import com.opa.transferservice.dto.submit.TransferSubmitRequest;
import com.opa.transferservice.dto.submit.TransferSubmitResponse;
import com.opa.transferservice.service.TransferReviewService;
import com.opa.transferservice.service.TransferSubmitService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.UUID;

import static org.springframework.web.servlet.function.ServerResponse.status;

@RestController
@RequestMapping("/v1/transfers")
@AllArgsConstructor
public class TransferController {

    private final TransferReviewService transferService;
    private final TransferSubmitService transferSubmitService;

    @GetMapping("/check")
    public ResponseEntity<String> checkHealth() {
        return ResponseEntity.ok("OK");
    }

    @PostMapping("/review")
    public ResponseEntity<ApiResponse<TransferReviewResponse>> reviewTransferController(
            @RequestBody TransferReviewRequest request
    ) {
        TransferReviewResponse response = transferService.reviewTransferService(request);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.<TransferReviewResponse>builder()
                        .success(true)
                        .message("Transfer review successful")
                        .data(response)
                        .traceId(UUID.randomUUID().toString())
                        .timestamp(Instant.now())
                        .build());
    }

    @PostMapping("/submit")
    public ResponseEntity<ApiResponse<TransferSubmitResponse>> submitTransferController(
            @RequestBody TransferSubmitRequest request
    ) {
        TransferSubmitResponse response = transferSubmitService.submitTransferService(request);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.<TransferSubmitResponse>builder()
                        .success(true)
                        .message("Transfer submitted successfully")
                        .data(response)
                        .traceId(UUID.randomUUID().toString())
                        .timestamp(Instant.now())
                        .build());
    }
}