package com.bizgenai.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "generations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Generation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_id", nullable = false)
    private Template template;

    @Column(columnDefinition = "CLOB")
    private String inputData;

    @Column(columnDefinition = "CLOB")
    private String assembledPrompt;

    @OneToMany(mappedBy = "generation", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<GenerationVariation> variations = new ArrayList<>();

    @Column(columnDefinition = "CLOB")
    private String disclaimers;

    @Column(columnDefinition = "CLOB")
    private String warnings;

    @Enumerated(EnumType.STRING)
    private GenerationStatus status;

    private Long generationTimeMs;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public void addVariation(GenerationVariation variation) {
        variations.add(variation);
        variation.setGeneration(this);
    }

    public void removeVariation(GenerationVariation variation) {
        variations.remove(variation);
        variation.setGeneration(null);
    }
}
