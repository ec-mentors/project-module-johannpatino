package io.everyonecodes.deliciousnessness.dto;

public record CreateRecipeIngredientRequest(
        String name,
        Double quantity,
        String unit,
        String preparation) {
}
