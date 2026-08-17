package io.everyonecodes.deliciousnessness.dto;

import io.everyonecodes.deliciousnessness.model.Category;
import io.everyonecodes.deliciousnessness.model.Recipe;
import io.everyonecodes.deliciousnessness.model.Season;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public record RecipeDto(
        Long id,
        String recipeName,
        Integer servings,
        Integer cookTimeMinutes,
        String instructions,
        Season season,
        String imageUrl,
        String sourceUrl,
        LocalDateTime createdAt,
        Set<String> categories,
        List<RecipeIngredientDto> ingredients) {

    public static RecipeDto from(Recipe recipe) {
        return new RecipeDto(
                recipe.getId(),
                recipe.getRecipeName(),
                recipe.getServings(),
                recipe.getCookTimeMinutes(),
                recipe.getInstructions(),
                recipe.getSeason(),
                recipe.getImageUrl(),
                recipe.getSourceUrl(),
                recipe.getCreatedAt(),
                recipe.getCategories().stream()
                        .map(Category::getName)
                        .collect(Collectors.toSet()),
                recipe.getRecipeIngredients().stream()
                        .map(RecipeIngredientDto::from)
                        .toList()
        );
    }
}
