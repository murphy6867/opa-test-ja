package com.opa.transferservice.service;

import com.opa.transferservice.client.AuthClient;
import com.opa.transferservice.client.OpaClient;
import com.opa.transferservice.dto.OpaDecision;
import com.opa.transferservice.dto.OpaError;
import com.opa.transferservice.dto.TransferRequest;
import com.opa.transferservice.dto.TransferResponse;
import com.opa.transferservice.dto.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class TransferService {

    @Value("${auth.url}")
    private String authUrl;

    private final OpaClient opaClient;
    private final RestTemplate restTemplate = new RestTemplate();

    public TransferService(AuthClient authClient, OpaClient opaClient) {
        this.opaClient = opaClient;
    }

    public User getUser(String username) {
        String url = authUrl + "/v1/auth/get-balance/" + username;
        return restTemplate.getForObject(url, User.class);
    }

    public ResponseEntity<TransferResponse> withdrawService(TransferRequest request) {
        User user = getUser(request.getUsername());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new TransferResponse("USER_NOT_FOUND", null, request.getUsername(), 0.0, "User not found")
            );
        }

        if (user.getBalance() < request.getAmount()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new TransferResponse("INSUFFICIENT_BALANCE", null, request.getUsername(), user.getBalance(), "Insufficient balance")
            );
        }

        final OpaDecision decision;
        try {
            decision = opaClient.checkTransfer(user.getRole(), request.getAmount());
        } catch (RestClientException ex) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(
                    new TransferResponse("OPA_UNAVAILABLE", null, request.getUsername(), user.getBalance(), "Policy service unavailable")
            );
        }

        if (decision == null) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(
                    new TransferResponse("OPA_UNAVAILABLE", null, request.getUsername(), user.getBalance(), "Policy decision unavailable")
            );
        }

        if (!decision.isAllow()) {
            OpaError err = decision.getError();

            HttpStatus status = HttpStatus.resolve(err.getHttpStatus());
            if (status == null) status = HttpStatus.FORBIDDEN;

            return ResponseEntity.status(status).body(
                    new TransferResponse(err.getCode(), null, request.getUsername(), user.getBalance(), err.getMessage())
            );
        }

        double newBalance = user.getBalance() - request.getAmount();
        return ResponseEntity.ok(
                new TransferResponse("SUCCESS", "TXN-" + System.currentTimeMillis(), request.getUsername(), newBalance, "Transfer completed successfully")
        );
    }
}