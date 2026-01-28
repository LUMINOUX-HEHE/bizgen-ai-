package com.bizgenai.controller;

import com.bizgenai.dto.request.GenerateContentRequest;
import com.bizgenai.dto.response.GenerationResponse;
import com.bizgenai.service.GenerationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/generate")
@Tag(name = "Generation", description = "Content generation endpoints")
public class GenerationController {

    private final GenerationService generationService;

    public GenerationController(GenerationService generationService) {
        this.generationService = generationService;
    }

    @PostMapping
    @Operation(
            summary = "Generate content",
            description = "Generate content based on template and user inputs"
    )
    @ApiResponse(responseCode = "200", description = "Content generated successfully")
    @ApiResponse(responseCode = "400", description = "Validation error")
    @ApiResponse(responseCode = "404", description = "Template not found")
    @ApiResponse(responseCode = "500", description = "Generation error")
    public ResponseEntity<GenerationResponse> generateContent(
            @Valid @RequestBody GenerateContentRequest request
    ) {
        return ResponseEntity.ok(generationService.generateContent(request));
    }

    @GetMapping("/{generationId}")
    @Operation(
            summary = "Get generation by ID",
            description = "Retrieve a specific generation by ID"
    )
    @ApiResponse(responseCode = "200", description = "Generation retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Generation not found")
    public ResponseEntity<GenerationResponse> getGeneration(@PathVariable String generationId) {
        return ResponseEntity.ok(generationService.getGenerationById(generationId));
    }
}
