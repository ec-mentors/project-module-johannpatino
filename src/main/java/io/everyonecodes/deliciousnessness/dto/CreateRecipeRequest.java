package io.everyonecodes.deliciousnessness.dto;

import io.everyonecodes.deliciousnessness.model.Language;
import io.everyonecodes.deliciousnessness.model.Season;

import java.util.List;
import java.util.Set;

public record CreateRecipeRequest(
        String recipeName,
        Integer servings,
        Integer cookTimeMinutes,
        String instructions,
        Season season,
        String imageUrl,
        String sourceUrl,
        Language languageCode,
        Set<String> categories,
        List<CreateRecipeIngredientRequest> ingredients) {
}
