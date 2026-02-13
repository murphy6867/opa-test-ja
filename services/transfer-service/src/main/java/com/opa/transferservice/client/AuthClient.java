package com.opa.transferservice.client;

import com.opa.transferservice.dto.OpaDecision;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class AuthClient {

    @Value("${auth.url}")
    private String authUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public OpaDecision authorize(String username, String action) {

        String url = authUrl + "/v1/auth/authorize";

        Map<String, Object> request = Map.of(
                "username", username,
                "action", action
        );

        return restTemplate.postForObject(
                url,
                request,
                OpaDecision.class
        );
    }
}
