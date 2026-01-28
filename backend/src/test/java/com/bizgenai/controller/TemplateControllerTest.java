package com.bizgenai.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TemplateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldGetAllTemplates() throws Exception {
        mockMvc.perform(get("/api/v1/templates"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].name").exists());
    }

    @Test
    void shouldGetTemplatesByCategory() throws Exception {
        mockMvc.perform(get("/api/v1/templates")
                        .param("categoryId", "cat-marketing"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void shouldGetTemplateById() throws Exception {
        mockMvc.perform(get("/api/v1/templates/tpl-instagram"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("tpl-instagram"))
                .andExpect(jsonPath("$.name").exists());
    }

    @Test
    void shouldGetTemplateSchema() throws Exception {
        mockMvc.perform(get("/api/v1/templates/tpl-instagram/schema"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.templateId").value("tpl-instagram"))
                .andExpect(jsonPath("$.sections").isArray())
                .andExpect(jsonPath("$.sections[0].fields").isArray());
    }

    @Test
    void shouldReturn404ForNonExistentTemplate() throws Exception {
        mockMvc.perform(get("/api/v1/templates/non-existent"))
                .andExpect(status().isNotFound());
    }
}
