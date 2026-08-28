package io.everyonecodes.deliciousnessness.repository;

import io.everyonecodes.deliciousnessness.dto.RecipeSummaryDto;
import io.everyonecodes.deliciousnessness.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    List<RecipeSummaryDto> findAllBy();

    List<RecipeSummaryDto> findByRecipeNameContainingIgnoreCase(String recipeName);

    @Query("""
            SELECT ri.recipe.id
            FROM RecipeIngredient  ri
            WHERE ri.ingredient.id IN :ingredientIds
            GROUP BY ri.recipe.id
            HAVING COUNT(DISTINCT ri.ingredient.id) = :requiredCount
            """)
    List<Long> findRecipeIdsWithAllIngredients(@Param("ingredientIds") Collection<Long> ingredientIds, @Param("requiredCount") long requiredCount);

    List<RecipeSummaryDto> findByIdIn(Collection<Long> recipeIds);

}
