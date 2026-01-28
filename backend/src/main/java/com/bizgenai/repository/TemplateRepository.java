package com.bizgenai.repository;

import com.bizgenai.entity.Template;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TemplateRepository extends JpaRepository<Template, String> {

    List<Template> findByActiveTrueOrderByDisplayOrderAsc();

    List<Template> findByCategoryIdAndActiveTrueOrderByDisplayOrderAsc(String categoryId);

    Optional<Template> findByIdAndActiveTrue(String id);

    @Query("SELECT t FROM Template t JOIN FETCH t.category WHERE t.id = :id AND t.active = true")
    Optional<Template> findByIdWithCategory(@Param("id") String id);

    @Query("SELECT t FROM Template t JOIN FETCH t.category WHERE t.active = true ORDER BY t.displayOrder")
    List<Template> findAllActiveWithCategory();

    @Query("SELECT t FROM Template t JOIN FETCH t.category c WHERE c.id = :categoryId AND t.active = true ORDER BY t.displayOrder")
    List<Template> findByCategoryIdWithCategory(@Param("categoryId") String categoryId);

    List<Template> findByPopularTrueAndActiveTrueOrderByDisplayOrderAsc();

    @Query("SELECT COUNT(t) FROM Template t WHERE t.category.id = :categoryId AND t.active = true")
    long countByCategoryId(@Param("categoryId") String categoryId);
}
