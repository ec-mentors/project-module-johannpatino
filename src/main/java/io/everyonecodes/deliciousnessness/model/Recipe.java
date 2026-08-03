package io.everyonecodes.deliciousnessness.model;

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

    @Column(nullable = false)
    private String recipeName;

    @Column(nullable = false)
    private Integer servings;

    @Column(nullable = false)
    private Integer cookTimeMinutes;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String instructions;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Season season;

    @Column(length = 2048)
    private String imageUrl;

    @Column(length = 2048)
    private String sourceUrl;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
