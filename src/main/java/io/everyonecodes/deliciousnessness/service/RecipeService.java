package io.everyonecodes.deliciousnessness.service;

import io.everyonecodes.deliciousnessness.model.Recipe;
import io.everyonecodes.deliciousnessness.repository.RecipeRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;

    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }


    public Recipe create(Recipe recipe) {
        recipe.setId(null);
        return recipeRepository.save(recipe);
    }

    public List<Recipe> findAll() {
        return recipeRepository.findAll();
    }

    public Optional<Recipe> findById(Long id) {
        return recipeRepository.findById(id);
    }

    @Transactional
    public Optional<Recipe> update(Long id, Recipe updatedRecipe) {
        return recipeRepository.findById(id).map(oldRecipe -> {
            oldRecipe.setRecipeName(updatedRecipe.getRecipeName());
            oldRecipe.setServings(updatedRecipe.getServings());
            oldRecipe.setCookTimeMinutes(updatedRecipe.getCookTimeMinutes());
            oldRecipe.setInstructions(updatedRecipe.getInstructions());
            oldRecipe.setSeason(updatedRecipe.getSeason());
            oldRecipe.setImageUrl(updatedRecipe.getImageUrl());
            oldRecipe.setSourceUrl(updatedRecipe.getSourceUrl());
            return oldRecipe;
        });
    }

    public boolean deleteById(Long id) {
        if (!recipeRepository.existsById(id)) {
            return false;
        }
        recipeRepository.deleteById(id);
        return true;
    }
}
