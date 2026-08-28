package io.everyonecodes.deliciousnessness.service;

import io.everyonecodes.deliciousnessness.dto.RecipeIngredientDto;
import io.everyonecodes.deliciousnessness.model.Ingredient;
import io.everyonecodes.deliciousnessness.model.IngredientName;
import io.everyonecodes.deliciousnessness.model.Language;
import io.everyonecodes.deliciousnessness.repository.IngredientNameRepository;
import io.everyonecodes.deliciousnessness.repository.IngredientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class IngredientService {

    private static final Language DEFAULT_LANGUAGE = Language.EN;

    private final IngredientRepository ingredientRepository;
    private final IngredientNameRepository ingredientNameRepository;

    public IngredientService(IngredientRepository ingredientRepository, IngredientNameRepository ingredientNameRepository) {
        this.ingredientRepository = ingredientRepository;
        this.ingredientNameRepository = ingredientNameRepository;
    }

    @Transactional
    public Ingredient findOrCreate(String name, Language language) {
        String normalisedName = name.trim().toLowerCase();
        Language resolvedLanguage = (language == null) ? DEFAULT_LANGUAGE : language;

        List<IngredientName> matches = ingredientNameRepository.findByNameIgnoreCase(normalisedName);

        if (!matches.isEmpty()) {
            return preferLanguage(matches, resolvedLanguage);
        }

        Ingredient ingredient = ingredientRepository.save(new Ingredient(normalisedName));
        ingredientNameRepository.save(new IngredientName(ingredient, normalisedName, language));
        return ingredient;
    }

    private Ingredient preferLanguage(List<IngredientName> matches, Language language) {
        return matches.stream()
                .filter(match -> match.getLanguageCode() == language)
                .findFirst()
                .orElse(matches.get(0))
                .getIngredient();
    }
}
