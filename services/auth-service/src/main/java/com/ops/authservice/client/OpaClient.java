package com.ops.authservice.client;

import com.ops.authservice.dto.OpaDecision;
import com.ops.authservice.dto.OpaResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class OpaClient {
    @Value("${opa.url}")
    private String opaUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public OpaDecision checkAccess(Map<String, Object> input) {

        String url = opaUrl + "/v1/data/auth/decision/result";

        Map<String, Object> request = Map.of("input", input);

        OpaResponse response = restTemplate.postForObject(
                url,
                request,
                OpaResponse.class
        );

        return response.getResult();
    }

}
