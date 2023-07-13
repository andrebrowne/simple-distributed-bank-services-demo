package com.vmware.tanzu.example.accountservice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@RestController
public class AccountServiceController {
    private WebClient auditWebClient;

    public AccountServiceController(@Value("${audit.service.url}") String auditServiceUrl) {
        auditWebClient = WebClient.create(auditServiceUrl);
    }

    @PostMapping("/balance")
    void balance(@RequestBody Map<String, Object> balanceBody) {
        auditWebClient.post()
                .uri("/entry")
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .bodyValue("{\"type\":\"DEBIT\",\"status\":\"SUCCESS\",\"amount\":" + balanceBody.get("amount") + "}")
                .retrieve().toBodilessEntity().block();
    }
}
