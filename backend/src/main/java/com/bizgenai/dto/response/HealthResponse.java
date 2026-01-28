package com.bizgenai.dto.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HealthResponse {

    private String status;
    private String version;
    private LocalDateTime timestamp;
    private Map<String, String> components;
}
