package io.everyonecodes.deliciousnessness.dto;

public record RecipeSummaryDto(
        Long id,
        String recipeName,
        Integer servings,
        Integer cookTimeMinutes,
        String imageUrl) {
}
