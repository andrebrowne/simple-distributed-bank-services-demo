package com.vmware.tanzu.example.debitservice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@RestController
public class DebitServiceController {
    private WebClient accountWebClient;
    private WebClient auditWebClient;

    public DebitServiceController(@Value("${account.service.url}") String accountServiceUrl, @Value("${audit.service.url}") String auditServiceUrl) {
        accountWebClient = WebClient.create(accountServiceUrl);
        auditWebClient = WebClient.create(auditServiceUrl);
    }

    @PostMapping("/purchase")
    void purchase(@RequestBody Map<String, Object> purchaseBody) {
        accountWebClient.post()
                .uri("/balance")
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .bodyValue("{\"amount\":" + purchaseBody.get("amount") + "}")
                .retrieve().toBodilessEntity().block();
        auditWebClient.post()
                .uri("/entry")
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .bodyValue("{\"type\":\"TRANSACTION\",\"status\":\"SUCCESS\",\"amount\":" + purchaseBody.get("amount") + "}")
                .retrieve().toBodilessEntity().block();
    }
}
