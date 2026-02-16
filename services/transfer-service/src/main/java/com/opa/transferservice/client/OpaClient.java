package com.opa.transferservice.client;

import com.opa.transferservice.dto.opa.OpaDecision;
import com.opa.transferservice.dto.opa.OpaResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Controller
public class OpaClient {

    @Value("${opa.url}")
    private String opaUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public OpaDecision checkTransfer(String role, double amount) {

        String url = opaUrl + "/v1/data/transfer/decision/result";

        Map<String, Object> input = Map.of(
                "role", role,
                "amount", amount
        );

        Map<String, Object> request = Map.of("input", input);

        OpaResponse response = restTemplate.postForObject(
                url,
                request,
                OpaResponse.class
        );

        return response.getResult();
    }
}
