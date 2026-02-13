package com.opa.transferservice.controller;

import com.opa.transferservice.dto.TransferRequest;
import com.opa.transferservice.dto.TransferResponse;
import com.opa.transferservice.service.TransferService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/transfer")
public class TransferController {

    private final TransferService transferService;

    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @GetMapping("/checkHealth")
    public ResponseEntity<String> checkHealth() {
        return ResponseEntity.ok("OK");
    }

    @PostMapping("/withdraw")
    public ResponseEntity<TransferResponse> withdrawController(@RequestBody TransferRequest request) {
        return transferService.withdrawService(request);
    }
}