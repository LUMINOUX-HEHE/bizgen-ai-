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
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldGetAllCategories() throws Exception {
        mockMvc.perform(get("/api/v1/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].name").exists())
                .andExpect(jsonPath("$[0].displayName").exists());
    }

    @Test
    void shouldGetCategoryById() throws Exception {
        mockMvc.perform(get("/api/v1/categories/cat-marketing"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("cat-marketing"))
                .andExpect(jsonPath("$.name").value("marketing"));
    }

    @Test
    void shouldReturn404ForNonExistentCategory() throws Exception {
        mockMvc.perform(get("/api/v1/categories/non-existent"))
                .andExpect(status().isNotFound());
    }
}
