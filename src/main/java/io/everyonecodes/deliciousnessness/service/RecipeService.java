package io.everyonecodes.deliciousnessness.service;

import io.everyonecodes.deliciousnessness.dto.CreateRecipeIngredientRequest;
import io.everyonecodes.deliciousnessness.dto.CreateRecipeRequest;
import io.everyonecodes.deliciousnessness.dto.RecipeDto;
import io.everyonecodes.deliciousnessness.dto.RecipeSummaryDto;
import io.everyonecodes.deliciousnessness.mapper.RecipeMapper;
import io.everyonecodes.deliciousnessness.model.Ingredient;
import io.everyonecodes.deliciousnessness.model.Recipe;
import io.everyonecodes.deliciousnessness.model.RecipeIngredient;
import io.everyonecodes.deliciousnessness.model.Season;
import io.everyonecodes.deliciousnessness.repository.RecipeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;


@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final RecipeMapper recipeMapper;
    private final CategoryService categoryService;
    private final IngredientService ingredientService;

    public RecipeService(RecipeRepository recipeRepository, RecipeMapper recipeMapper, CategoryService categoryService, IngredientService ingredientService) {
        this.recipeRepository = recipeRepository;
        this.recipeMapper = recipeMapper;
        this.categoryService = categoryService;
        this.ingredientService = ingredientService;
    }

    @Transactional
    public RecipeDto create(CreateRecipeRequest request) {
        Recipe recipe = new Recipe();

        copyFields(request, recipe);
        replaceCategories(request, recipe);
        replaceIngredients(request, recipe);

        return recipeMapper.toDto(recipeRepository.save(recipe));
    }

    @Transactional(readOnly = true)
    public Optional<RecipeDto> findById(Long id) {
        return recipeRepository.findById(id)
                .map(recipeMapper::toDto);
    }

    @Transactional(readOnly = true)
    public List<RecipeSummaryDto> search(String query, List<Long> ingredientIds, List<Long> categoryIds, Season season) {
        Set<Long> matchingIds = null;

        if (query != null && !query.isBlank()) {
            matchingIds = narrow(matchingIds, recipeRepository.findIdsByNameContaining(query.trim()));
        }
        if (ingredientIds != null && !ingredientIds.isEmpty()) {
            Set<Long> distinctIngredientIds = new LinkedHashSet<>(ingredientIds);
            matchingIds = narrow(matchingIds, recipeRepository.findRecipeIdsWithAllIngredients(distinctIngredientIds, distinctIngredientIds.size()));
        }

        if (categoryIds != null && !categoryIds.isEmpty()) {
            matchingIds = narrow(matchingIds, recipeRepository.findIdsByAnyCategory(categoryIds));
        }

        if (season != null) {
            matchingIds = narrow(matchingIds, recipeRepository.findIdsBySeason(season));
        }

        if (matchingIds == null) {
            return recipeRepository.findAllBy();
        }
        if (matchingIds.isEmpty()) {
            return List.of();
        }

        return recipeRepository.findByIdIn(matchingIds);
    }

    @Transactional
    public Optional<RecipeDto> update(Long id, CreateRecipeRequest request) {
        return recipeRepository.findById(id)
                .map(recipe ->
                {
                    copyFields(request, recipe);
                    replaceCategories(request, recipe);
                    replaceIngredients(request, recipe);
                    return recipeMapper.toDto(recipe);
                });
    }


    public boolean deleteById(Long id) {
        if (!recipeRepository.existsById(id)) {
            return false;
        }
        recipeRepository.deleteById(id);
        return true;
    }

    private void copyFields(CreateRecipeRequest request, Recipe recipe) {
        recipe.setRecipeName((request.recipeName()));
        recipe.setServings(request.servings());
        recipe.setCookTimeMinutes(request.cookTimeMinutes());
        recipe.setInstructions(request.instructions());
        recipe.setSeason(request.season());
        recipe.setImageUrl(request.imageUrl());
        recipe.setSourceUrl(request.sourceUrl());
    }

    private void replaceCategories(CreateRecipeRequest request, Recipe recipe) {
        recipe.getCategories().clear();
        recipe.getCategories().addAll(categoryService.findOrCreateAll(request.categories()));
    }

    private void replaceIngredients(CreateRecipeRequest request, Recipe recipe) {
        recipe.getRecipeIngredients().clear();

        if (request.ingredients() == null) {
            return;
        }

        for (CreateRecipeIngredientRequest line : request.ingredients()) {
            Ingredient ingredient = ingredientService.findOrCreate(line.name(), request.languageCode());

            recipe.addIngredient(new RecipeIngredient(ingredient, line.quantity(), line.unit(), line.preparation()));
        }
    }

    private Set<Long> narrow(Set<Long> current, List<Long> matches) {
        if (current == null) {
            return new LinkedHashSet<>(matches);
        }
        current.retainAll(matches);
        return current;
    }
}
