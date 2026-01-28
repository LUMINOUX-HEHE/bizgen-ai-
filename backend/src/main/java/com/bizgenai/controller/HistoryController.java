package com.bizgenai.controller;

import com.bizgenai.dto.response.GenerationResponse;
import com.bizgenai.dto.response.HistoryItemResponse;
import com.bizgenai.dto.response.PageResponse;
import com.bizgenai.service.HistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/history")
@Tag(name = "History", description = "Generation history endpoints")
public class HistoryController {

    private final HistoryService historyService;

    public HistoryController(HistoryService historyService) {
        this.historyService = historyService;
    }

    @GetMapping
    @Operation(
            summary = "Get generation history",
            description = "Get paginated generation history with optional filters"
    )
    @ApiResponse(responseCode = "200", description = "History retrieved successfully")
    public ResponseEntity<PageResponse<HistoryItemResponse>> getHistory(
            @Parameter(description = "Page number (0-based)")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size (max 100)")
            @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Filter by category ID")
            @RequestParam(required = false) String categoryId,
            @Parameter(description = "Filter by template ID")
            @RequestParam(required = false) String templateId,
            @Parameter(description = "Filter from date (ISO format)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @Parameter(description = "Filter to date (ISO format)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate
    ) {
        return ResponseEntity.ok(
                historyService.getHistory(page, size, categoryId, templateId, fromDate, toDate)
        );
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get history item",
            description = "Get a single history item with full details"
    )
    @ApiResponse(responseCode = "200", description = "History item retrieved successfully")
    @ApiResponse(responseCode = "404", description = "History item not found")
    public ResponseEntity<GenerationResponse> getHistoryItem(@PathVariable String id) {
        return ResponseEntity.ok(historyService.getHistoryItem(id));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete history item",
            description = "Delete a history item"
    )
    @ApiResponse(responseCode = "204", description = "History item deleted successfully")
    @ApiResponse(responseCode = "404", description = "History item not found")
    public ResponseEntity<Void> deleteHistoryItem(@PathVariable String id) {
        historyService.deleteHistoryItem(id);
        return ResponseEntity.noContent().build();
    }
}
