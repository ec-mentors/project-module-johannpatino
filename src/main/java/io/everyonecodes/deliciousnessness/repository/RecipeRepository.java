package io.everyonecodes.deliciousnessness.repository;

import io.everyonecodes.deliciousnessness.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {

}
