package io.everyonecodes.deliciousnessness.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"name", "language_code"}))
@Getter
@Setter
@NoArgsConstructor
public class IngredientName {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // FK to Ingredient
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Ingredient ingredient;

    @Column(nullable = false, length = 128)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 8)
    private Language languageCode;

    public IngredientName(Ingredient ingredient, String name, Language languageCode) {
        this.ingredient = ingredient;
        this.name = name;
        this.languageCode = languageCode;
    }
}
