package io.everyonecodes.deliciousnessness.dto;

import io.everyonecodes.deliciousnessness.model.Season;

public record RecipeSummaryDto(
        Long id,
        String recipeName,
        Integer servings,
        Integer cookTimeMinutes,
        Season season,
        String imageUrl) {
}
