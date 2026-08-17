package io.everyonecodes.deliciousnessness.dto;

import io.everyonecodes.deliciousnessness.model.RecipeIngredient;

public record RecipeIngredientDto(
        Long ingredientId,
        String ingredientName,
        Double quantity,
        String unit,
        String preparation) {

    public static RecipeIngredientDto from(RecipeIngredient recipeIngredient) {
        return new RecipeIngredientDto(
                recipeIngredient.getIngredient().getId(),
                recipeIngredient.getIngredient().getCanonicalName(),
                recipeIngredient.getQuantity(),
                recipeIngredient.getUnit(),
                recipeIngredient.getPreparation());
    }
}
