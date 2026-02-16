package com.opa.transferservice.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.opa.transferservice.client.model.AccountResponse;
import com.opa.transferservice.client.model.AuthApiResponse;
import com.opa.transferservice.client.model.BatchAccountRequest;
import com.opa.transferservice.client.model.DebitAccountRequest;
import com.opa.transferservice.client.model.DebitAccountResponse;

import java.util.List;
import java.util.UUID;

@Component
public class AuthClient {

    @Value("${auth.url}")
    private String authUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public String getRoleByUserId(UUID userId) {
        String url = authUrl + "/v1/auth/getRoleByUserId/" + userId;

        ResponseEntity<AuthApiResponse<String>> resp = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );

        AuthApiResponse<String> body = resp.getBody();
        if (body == null || body.getData() == null || body.getData().isBlank()) {
            return "STANDARD";
        }
        return body.getData();
    }

    public List<AccountResponse> getAccountsByNumbers(List<String> accountNumbers) {
        String url = authUrl + "/v1/accounts/batch-accounts";

        BatchAccountRequest request = new BatchAccountRequest(accountNumbers);
        HttpEntity<BatchAccountRequest> entity = new HttpEntity<>(request);

        ResponseEntity<AuthApiResponse<List<AccountResponse>>> resp = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                new ParameterizedTypeReference<>() {}
        );

        AuthApiResponse<List<AccountResponse>> body = resp.getBody();
        if (body == null || body.getData() == null) {
            return List.of();
        }
        return body.getData();
    }

    public DebitAccountResponse debitAccount(String accountNumber, java.math.BigDecimal amount, String reference) {
        String url = authUrl + "/v1/accounts/debit";

        DebitAccountRequest request = new DebitAccountRequest(accountNumber, amount, reference);
        HttpEntity<DebitAccountRequest> entity = new HttpEntity<>(request);

        ResponseEntity<AuthApiResponse<DebitAccountResponse>> resp = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                new ParameterizedTypeReference<>() {}
        );

        AuthApiResponse<DebitAccountResponse> body = resp.getBody();
        if (body == null || body.getData() == null) {
            throw new IllegalStateException("Debit API returned null response");
        }
        return body.getData();
    }
}
