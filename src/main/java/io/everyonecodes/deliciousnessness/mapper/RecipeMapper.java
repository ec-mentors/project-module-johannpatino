package io.everyonecodes.deliciousnessness.mapper;

import io.everyonecodes.deliciousnessness.dto.CategoryDto;
import io.everyonecodes.deliciousnessness.dto.RecipeDto;
import io.everyonecodes.deliciousnessness.dto.RecipeIngredientDto;
import io.everyonecodes.deliciousnessness.model.Category;
import io.everyonecodes.deliciousnessness.model.Ingredient;
import io.everyonecodes.deliciousnessness.model.Recipe;
import io.everyonecodes.deliciousnessness.model.RecipeIngredient;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class RecipeMapper {
    public RecipeDto toDto(Recipe recipe) {
        Set<CategoryDto> categories = recipe.getCategories()
                .stream()
                .map(this::toCategoryDto)
                .collect(Collectors.toSet());

        List<RecipeIngredientDto> ingredients = recipe.getRecipeIngredients()
                .stream().map(this::toIngredientDto)
                .toList();

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
                categories,
                ingredients);
    }
    public CategoryDto toCategoryDto(Category category){
        return new CategoryDto(category.getId(), category.getName());
    }

    public RecipeIngredientDto toIngredientDto(RecipeIngredient recipeIngredient){
        Ingredient ingredient = recipeIngredient.getIngredient();

        return new RecipeIngredientDto(
                ingredient.getId(),
                ingredient.getCanonicalName(),
                recipeIngredient.getQuantity(),
                recipeIngredient.getUnit(),
                recipeIngredient.getPreparation());
    }
}

// mapstruct