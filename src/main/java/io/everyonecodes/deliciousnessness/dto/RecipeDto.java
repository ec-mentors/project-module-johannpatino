package io.everyonecodes.deliciousnessness.dto;


import io.everyonecodes.deliciousnessness.model.Season;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

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
        Set<CategoryDto> categories,
        List<RecipeIngredientDto> ingredients) {

}