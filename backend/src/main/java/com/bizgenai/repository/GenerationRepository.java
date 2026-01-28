package com.bizgenai.repository;

import com.bizgenai.entity.Generation;
import com.bizgenai.entity.GenerationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface GenerationRepository extends JpaRepository<Generation, String> {

    @Query("SELECT g FROM Generation g JOIN FETCH g.template t JOIN FETCH t.category WHERE g.id = :id")
    Optional<Generation> findByIdWithTemplateAndCategory(@Param("id") String id);

    @Query("SELECT g FROM Generation g JOIN FETCH g.template t JOIN FETCH t.category ORDER BY g.createdAt DESC")
    Page<Generation> findAllWithTemplateAndCategory(Pageable pageable);

    @Query("SELECT g FROM Generation g JOIN FETCH g.template t JOIN FETCH t.category c WHERE c.id = :categoryId ORDER BY g.createdAt DESC")
    Page<Generation> findByCategoryId(@Param("categoryId") String categoryId, Pageable pageable);

    @Query("SELECT g FROM Generation g JOIN FETCH g.template t WHERE t.id = :templateId ORDER BY g.createdAt DESC")
    Page<Generation> findByTemplateId(@Param("templateId") String templateId, Pageable pageable);

    @Query("SELECT g FROM Generation g WHERE g.createdAt BETWEEN :fromDate AND :toDate ORDER BY g.createdAt DESC")
    Page<Generation> findByDateRange(@Param("fromDate") LocalDateTime fromDate, 
                                      @Param("toDate") LocalDateTime toDate, 
                                      Pageable pageable);

    List<Generation> findByStatus(GenerationStatus status);

    @Query("SELECT g FROM Generation g JOIN FETCH g.variations WHERE g.id = :id")
    Optional<Generation> findByIdWithVariations(@Param("id") String id);

    long countByTemplateId(String templateId);

    void deleteByCreatedAtBefore(LocalDateTime date);
}
