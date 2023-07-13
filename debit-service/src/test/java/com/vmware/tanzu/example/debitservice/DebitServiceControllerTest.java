package com.vmware.tanzu.example.debitservice;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.TestSocketUtils;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "account.service.url=http://localhost:${test.account-service.port}",
        "audit.service.url=http://localhost:${test.audit-service.port}",
})
class DebitServiceControllerTest {
    private static MockWebServer mockAccountServer;
    private static MockWebServer mockAuditServer;

    @Autowired
    MockMvc mockMvc;

    @BeforeAll
    static void setUp() throws IOException {
        int accountServicePort = TestSocketUtils.findAvailableTcpPort();
        int auditServicePort = TestSocketUtils.findAvailableTcpPort();

        System.setProperty("test.account-service.port", String.valueOf(accountServicePort));
        System.setProperty("test.audit-service.port", String.valueOf(auditServicePort));

        mockAccountServer = new MockWebServer();
        mockAccountServer.start(accountServicePort);
        mockAuditServer = new MockWebServer();
        mockAuditServer.start(auditServicePort);
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockAccountServer.shutdown();
        mockAuditServer.shutdown();

        System.clearProperty("test.account-service.port");
        System.clearProperty("test.audit-service.port");
    }

    @Test
    @DisplayName("Given a customer makes a purchase, then their account is updated and all events are audited")
    void postPurchase() throws Exception {
        mockAccountServer.enqueue(new MockResponse().setResponseCode(200));
        mockAuditServer.enqueue(new MockResponse().setResponseCode(200));

        mockMvc.perform(post("/purchase")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amount\": 5}"))
                .andExpect(status().isOk());

        RecordedRequest accountRequest = mockAccountServer.takeRequest();
        assertThat(accountRequest.getMethod()).isEqualTo("POST");
        assertThat(accountRequest.getPath()).isEqualTo("/balance");
        assertThat(accountRequest.getBody().readUtf8()).isEqualTo("{\"amount\":5}");
        assertThat(accountRequest.getHeader("Content-Type")).contains(MediaType.APPLICATION_JSON_VALUE);

        RecordedRequest auditRequest = mockAuditServer.takeRequest();
        assertThat(auditRequest.getMethod()).isEqualTo("POST");
        assertThat(auditRequest.getPath()).isEqualTo("/entry");
        assertThat(auditRequest.getBody().readUtf8()).isEqualTo("{\"type\":\"TRANSACTION\",\"status\":\"SUCCESS\",\"amount\":5}");
        assertThat(auditRequest.getHeader("Content-Type")).contains(MediaType.APPLICATION_JSON_VALUE);
    }
}
