package com.bizgenai.service;

import com.bizgenai.dto.mapper.GenerationMapper;
import com.bizgenai.dto.response.GenerationResponse;
import com.bizgenai.dto.response.HistoryItemResponse;
import com.bizgenai.dto.response.PageResponse;
import com.bizgenai.entity.Generation;
import com.bizgenai.exception.ResourceNotFoundException;
import com.bizgenai.repository.GenerationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class HistoryService {

    private static final Logger log = LoggerFactory.getLogger(HistoryService.class);
    private final GenerationRepository generationRepository;
    private final GenerationMapper generationMapper;

    public HistoryService(GenerationRepository generationRepository, GenerationMapper generationMapper) {
        this.generationRepository = generationRepository;
        this.generationMapper = generationMapper;
    }

    public PageResponse<HistoryItemResponse> getHistory(
            int page,
            int size,
            String categoryId,
            String templateId,
            LocalDate fromDate,
            LocalDate toDate
    ) {
        log.debug("Fetching history - page: {}, size: {}, categoryId: {}, templateId: {}",
                page, size, categoryId, templateId);

        size = Math.min(size, 100); // Max 100 items per page
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<Generation> generations;

        if (categoryId != null) {
            generations = generationRepository.findByCategoryId(categoryId, pageable);
        } else if (templateId != null) {
            generations = generationRepository.findByTemplateId(templateId, pageable);
        } else if (fromDate != null && toDate != null) {
            LocalDateTime from = fromDate.atStartOfDay();
            LocalDateTime to = toDate.atTime(LocalTime.MAX);
            generations = generationRepository.findByDateRange(from, to, pageable);
        } else {
            generations = generationRepository.findAllWithTemplateAndCategory(pageable);
        }

        List<HistoryItemResponse> items = generationMapper.toHistoryItemResponseList(generations.getContent());

        return PageResponse.of(items, page, size, generations.getTotalElements());
    }

    public GenerationResponse getHistoryItem(String id) {
        log.debug("Fetching history item: {}", id);
        Generation generation = generationRepository.findByIdWithTemplateAndCategory(id)
                .orElseThrow(() -> new ResourceNotFoundException("History item", "id", id));

        return generationMapper.toResponse(generation);
    }

    @Transactional
    public void deleteHistoryItem(String id) {
        log.info("Deleting history item: {}", id);
        if (!generationRepository.existsById(id)) {
            throw new ResourceNotFoundException("History item", "id", id);
        }
        generationRepository.deleteById(id);
    }

    @Transactional
    public void deleteOldHistory(int daysToKeep) {
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(daysToKeep);
        log.info("Deleting history items older than: {}", cutoffDate);
        generationRepository.deleteByCreatedAtBefore(cutoffDate);
    }
}
