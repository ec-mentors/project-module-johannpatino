package io.everyonecodes.deliciousnessness.repository;

import io.everyonecodes.deliciousnessness.dto.IngredientDto;
import io.everyonecodes.deliciousnessness.model.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {

    List<IngredientDto> findByIdIn(Collection<Long> ids);

    Optional<Ingredient> findByCanonicalNameIgnoreCase(String canonicalName);

}
