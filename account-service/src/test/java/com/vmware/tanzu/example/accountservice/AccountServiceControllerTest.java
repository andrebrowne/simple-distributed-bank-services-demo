package com.vmware.tanzu.example.accountservice;

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
        "audit.service.url=http://localhost:${test.audit-service.port}",
})
class AccountServiceControllerTest {
    private static MockWebServer mockAuditServer;

    @Autowired
    MockMvc mockMvc;

    @BeforeAll
    static void setUp() throws IOException {
        int auditServicePort = TestSocketUtils.findAvailableTcpPort();

        System.setProperty("test.audit-service.port", String.valueOf(auditServicePort));

        mockAuditServer = new MockWebServer();
        mockAuditServer.start(auditServicePort);
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockAuditServer.shutdown();
        System.clearProperty("test.audit-service.port");
    }

    @Test
    @DisplayName("Given an account balance update is required, then the balance updated event is audited")
    void postBalance() throws Exception {
        mockAuditServer.enqueue(new MockResponse().setResponseCode(200));

        mockMvc.perform(post("/balance")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amount\": 5}"))
                .andExpect(status().isOk());

        RecordedRequest auditRequest = mockAuditServer.takeRequest();
        assertThat(auditRequest.getMethod()).isEqualTo("POST");
        assertThat(auditRequest.getPath()).isEqualTo("/entry");
        assertThat(auditRequest.getHeader("Content-Type")).contains(MediaType.APPLICATION_JSON_VALUE);
        assertThat(auditRequest.getBody().readUtf8()).isEqualTo("{\"type\":\"DEBIT\",\"status\":\"SUCCESS\",\"amount\":5}");
    }
}
