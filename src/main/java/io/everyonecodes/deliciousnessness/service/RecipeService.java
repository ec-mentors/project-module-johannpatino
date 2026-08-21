package io.everyonecodes.deliciousnessness.service;

import io.everyonecodes.deliciousnessness.dto.RecipeDto;
import io.everyonecodes.deliciousnessness.dto.RecipeSummaryDto;
import io.everyonecodes.deliciousnessness.mapper.RecipeMapper;
import io.everyonecodes.deliciousnessness.model.Recipe;
import io.everyonecodes.deliciousnessness.repository.RecipeRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final RecipeMapper recipeMapper;

    public RecipeService(RecipeRepository recipeRepository, RecipeMapper recipeMapper) {
        this.recipeRepository = recipeRepository;
        this.recipeMapper = recipeMapper;
    }


    @Transactional
    public RecipeDto create(Recipe recipe) {
        recipe.setId(null);
        return recipeMapper.toDto(recipeRepository.save(recipe));
    }

    @Transactional(readOnly = true)
    public List<RecipeSummaryDto> findAll() {
        return recipeRepository.findAllBy();
    }

    @Transactional(readOnly = true)
    public Optional<RecipeDto> findById(Long id) {
        return recipeRepository.findById(id)
                .map(recipeMapper::toDto);
    }

    @Transactional
    public Optional<RecipeDto> update(Long id, Recipe updatedRecipe) {
        return recipeRepository.findById(id).map(oldRecipe -> {
            oldRecipe.setRecipeName(updatedRecipe.getRecipeName());
            oldRecipe.setServings(updatedRecipe.getServings());
            oldRecipe.setCookTimeMinutes(updatedRecipe.getCookTimeMinutes());
            oldRecipe.setInstructions(updatedRecipe.getInstructions());
            oldRecipe.setSeason(updatedRecipe.getSeason());
            oldRecipe.setImageUrl(updatedRecipe.getImageUrl());
            oldRecipe.setSourceUrl(updatedRecipe.getSourceUrl());
            return recipeMapper.toDto(oldRecipe);
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
