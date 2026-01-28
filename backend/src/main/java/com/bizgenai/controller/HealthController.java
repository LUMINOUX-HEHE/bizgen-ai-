package com.bizgenai.controller;

import com.bizgenai.ai.AiService;
import com.bizgenai.dto.response.HealthResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/health")
@Tag(name = "Health", description = "Health check endpoint")
public class HealthController {

    private final DataSource dataSource;
    private final AiService aiService;

    @Value("${spring.application.version:1.0.0}")
    private String version;

    public HealthController(DataSource dataSource, AiService aiService) {
        this.dataSource = dataSource;
        this.aiService = aiService;
    }

    @GetMapping
    @Operation(
            summary = "Health check",
            description = "Check the health status of the application and its components"
    )
    @ApiResponse(responseCode = "200", description = "Application is healthy")
    public ResponseEntity<HealthResponse> health() {
        Map<String, String> components = new HashMap<>();

        // Check database
        try (Connection connection = dataSource.getConnection()) {
            if (connection.isValid(5)) {
                components.put("database", "UP");
            } else {
                components.put("database", "DOWN");
            }
        } catch (Exception e) {
            components.put("database", "DOWN");
        }

        // Check AI service
        components.put("aiService", aiService.isAvailable() ? "UP" : "DOWN");

        // Determine overall status
        boolean allUp = components.values().stream().allMatch("UP"::equals);

        HealthResponse response = HealthResponse.builder()
                .status(allUp ? "UP" : "DEGRADED")
                .version(version)
                .timestamp(LocalDateTime.now())
                .components(components)
                .build();

        return ResponseEntity.ok(response);
    }
}
