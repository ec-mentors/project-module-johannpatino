package io.everyonecodes.deliciousnessness.model;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "recipe_name", nullable = false)
    private String recipeName;

    @Column(nullable = false)
    private Integer servings;

    @Column(name = "cook_time_minutes", nullable = false)
    private Integer cookTimeMinutes;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String instructions;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Season season;

    @Column(name = "image_url", length = 2048)
    private String imageUrl;

    @Column(name = "source_url", length = 2048)
    private String sourceUrl;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
