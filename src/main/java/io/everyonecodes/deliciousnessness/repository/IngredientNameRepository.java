package io.everyonecodes.deliciousnessness.repository;

import io.everyonecodes.deliciousnessness.model.IngredientName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IngredientNameRepository extends JpaRepository<IngredientName, Long> {

    List<IngredientName> findByNameIgnoreCase(String name);

    List<IngredientName> findByIngredientId(Long ingredientId);
}
