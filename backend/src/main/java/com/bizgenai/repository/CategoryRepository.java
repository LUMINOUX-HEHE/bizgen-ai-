package com.bizgenai.repository;

import com.bizgenai.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {

    List<Category> findByActiveTrueOrderByDisplayOrderAsc();

    Optional<Category> findByName(String name);

    Optional<Category> findByIdAndActiveTrue(String id);

    @Query("SELECT c FROM Category c LEFT JOIN FETCH c.templates WHERE c.active = true ORDER BY c.displayOrder")
    List<Category> findAllActiveWithTemplates();

    boolean existsByName(String name);
}
