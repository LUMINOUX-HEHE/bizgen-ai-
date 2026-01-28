package com.bizgenai.controller;

import com.bizgenai.dto.response.TemplateResponse;
import com.bizgenai.dto.response.TemplateSchemaResponse;
import com.bizgenai.service.TemplateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/templates")
@Tag(name = "Templates", description = "Template management endpoints")
public class TemplateController {

    private final TemplateService templateService;

    public TemplateController(TemplateService templateService) {
        this.templateService = templateService;
    }

    @GetMapping
    @Operation(
            summary = "Get all templates",
            description = "Retrieve all templates, optionally filtered by category"
    )
    @ApiResponse(responseCode = "200", description = "Templates retrieved successfully")
    public ResponseEntity<List<TemplateResponse>> getAllTemplates(
            @Parameter(description = "Filter by category ID")
            @RequestParam(required = false) String categoryId,
            @Parameter(description = "Filter by active status")
            @RequestParam(required = false, defaultValue = "true") Boolean active
    ) {
        return ResponseEntity.ok(templateService.getAllTemplates(categoryId, active));
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get template by ID",
            description = "Retrieve a single template by its ID"
    )
    @ApiResponse(responseCode = "200", description = "Template retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Template not found")
    public ResponseEntity<TemplateResponse> getTemplateById(@PathVariable String id) {
        return ResponseEntity.ok(templateService.getTemplateById(id));
    }

    @GetMapping("/{id}/schema")
    @Operation(
            summary = "Get template schema",
            description = "Get the form schema for a template (parsed from blueprint)"
    )
    @ApiResponse(responseCode = "200", description = "Schema retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Template not found")
    public ResponseEntity<TemplateSchemaResponse> getTemplateSchema(@PathVariable String id) {
        return ResponseEntity.ok(templateService.getTemplateSchema(id));
    }

    @GetMapping("/popular")
    @Operation(
            summary = "Get popular templates",
            description = "Retrieve templates marked as popular"
    )
    @ApiResponse(responseCode = "200", description = "Popular templates retrieved successfully")
    public ResponseEntity<List<TemplateResponse>> getPopularTemplates() {
        return ResponseEntity.ok(templateService.getPopularTemplates());
    }
}
