package com.vmware.tanzu.example.auditservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@Slf4j
public class AuditServiceController {

    @PostMapping("/entry")
    void entry(@RequestBody Map<String, Object> entryBody) {
        log.info(entryBody.toString());
    }
}
