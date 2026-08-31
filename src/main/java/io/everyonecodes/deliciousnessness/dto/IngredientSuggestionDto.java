package io.everyonecodes.deliciousnessness.dto;

import io.everyonecodes.deliciousnessness.model.Language;

public record IngredientSuggestionDto(
        Long id,
        String canonicalName,
        String matchedName,
        Language matchedLanguage) {
}
