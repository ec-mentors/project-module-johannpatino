package io.everyonecodes.deliciousnessness.repository;

import io.everyonecodes.deliciousnessness.model.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {

    Optional<Ingredient> findByCanonicalNameIgnoreCase(String canonicalName);
}
