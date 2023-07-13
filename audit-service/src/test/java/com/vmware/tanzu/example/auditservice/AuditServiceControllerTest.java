package com.vmware.tanzu.example.auditservice;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(OutputCaptureExtension.class)
class AuditServiceControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("Given a service publishes an event, then the published event is logged to standard out")
    void postEntry(CapturedOutput output) throws Exception {
        mockMvc.perform(post("/entry")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"type\":\"ENTRY_TYPE\",\"status\":\"SUCCESS\",\"answer\": 42}"))
                .andExpect(status().isOk());

        assertThat(output).contains("type=ENTRY_TYPE, status=SUCCESS, answer=42");
    }
}
