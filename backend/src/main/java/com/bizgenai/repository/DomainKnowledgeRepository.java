package com.bizgenai.repository;

import com.bizgenai.entity.DomainKnowledge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DomainKnowledgeRepository extends JpaRepository<DomainKnowledge, String> {

    Optional<DomainKnowledge> findByReferenceId(String referenceId);

    List<DomainKnowledge> findByReferenceIdIn(List<String> referenceIds);

    List<DomainKnowledge> findByActiveTrue();

    boolean existsByReferenceId(String referenceId);
}
