package io.everyonecodes.deliciousnessness.service;

import io.everyonecodes.deliciousnessness.dto.RecipeIngredientDto;
import io.everyonecodes.deliciousnessness.model.Ingredient;
import io.everyonecodes.deliciousnessness.model.IngredientName;
import io.everyonecodes.deliciousnessness.repository.IngredientNameRepository;
import io.everyonecodes.deliciousnessness.repository.IngredientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class IngredientService {

    private static final String DEFAULT_LANGUAGE = "en";

    private final IngredientRepository ingredientRepository;
    private final IngredientNameRepository ingredientNameRepository;

    public IngredientService(IngredientRepository ingredientRepository, IngredientNameRepository ingredientNameRepository) {
        this.ingredientRepository = ingredientRepository;
        this.ingredientNameRepository = ingredientNameRepository;
    }

    @Transactional
    public Ingredient findOrCreate(String name, String languageCode) {
        String normalisedName = name.trim().toLowerCase();
        String language = normaliseLanguage(languageCode);

        List<IngredientName> matches = ingredientNameRepository.findByNameIgnoreCase(normalisedName);

        if (!matches.isEmpty()) {
            return preferLanguage(matches, language);
        }

        Ingredient ingredient = ingredientRepository.save(new Ingredient(normalisedName));
        ingredientNameRepository.save(new IngredientName(ingredient, normalisedName, language));
        return ingredient;
    }

    private String normaliseLanguage(String languageCode) {
        return (languageCode == null || languageCode.isBlank())
                ? DEFAULT_LANGUAGE
                : languageCode.trim().toLowerCase();
    }

    private Ingredient preferLanguage(List<IngredientName> matches, String language) {
        return matches.stream()
                .filter(match -> match.getLanguageCode().equalsIgnoreCase(language))
                .findFirst()
                .orElse(matches.get(0))
                .getIngredient();
    }
}
