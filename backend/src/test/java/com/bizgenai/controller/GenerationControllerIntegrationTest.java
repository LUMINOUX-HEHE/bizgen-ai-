package com.bizgenai.controller;

import com.bizgenai.dto.request.GenerateContentRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class GenerationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldGenerateContentSuccessfully() throws Exception {
        GenerateContentRequest request = GenerateContentRequest.builder()
                .templateId("tpl-instagram")
                .inputs(Map.of(
                        "productName", "Summer Collection 2024",
                        "keyBenefits", "Sustainable, affordable, and stylish summer wear",
                        "targetAudience", "Young professionals",
                        "tone", "casual",
                        "callToAction", "link-in-bio"
                ))
                .build();

        mockMvc.perform(post("/api/v1/generate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.generationId").exists())
                .andExpect(jsonPath("$.variations").isArray())
                .andExpect(jsonPath("$.variations.length()").value(3));
    }

    @Test
    void shouldReturnBadRequestForMissingTemplateId() throws Exception {
        GenerateContentRequest request = GenerateContentRequest.builder()
                .inputs(Map.of("productName", "Test"))
                .build();

        mockMvc.perform(post("/api/v1/generate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"));
    }

    @Test
    void shouldReturnNotFoundForInvalidTemplateId() throws Exception {
        GenerateContentRequest request = GenerateContentRequest.builder()
                .templateId("invalid-template-id")
                .inputs(Map.of("productName", "Test"))
                .build();

        mockMvc.perform(post("/api/v1/generate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }
}
