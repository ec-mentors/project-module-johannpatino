package io.everyonecodes.deliciousnessness.service;

import io.everyonecodes.deliciousnessness.dto.IngredientSuggestionDto;
import io.everyonecodes.deliciousnessness.model.Ingredient;
import io.everyonecodes.deliciousnessness.model.IngredientName;
import io.everyonecodes.deliciousnessness.model.Language;
import io.everyonecodes.deliciousnessness.repository.IngredientNameRepository;
import io.everyonecodes.deliciousnessness.repository.IngredientRepository;
import org.springframework.data.domain.Limit;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

    @Transactional(readOnly = true)
    public List<IngredientSuggestionDto> autocomplete(String prefix) {
        if (prefix == null || prefix.isBlank()) {
            return List.of();
        }
        List<IngredientSuggestionDto> matches = ingredientNameRepository.searchByName(prefix.trim().toLowerCase(), Limit.of(20));

        Map<Long, IngredientSuggestionDto> byIngredientId = new LinkedHashMap<>();
        for (IngredientSuggestionDto match : matches) {
            byIngredientId.putIfAbsent(match.id(), match);
        }
        return byIngredientId.values().stream().limit(10).toList();
    }

    private Ingredient preferLanguage(List<IngredientName> matches, Language language) {
        return matches.stream()
                .filter(match -> match.getLanguageCode() == language)
                .findFirst()
                .orElse(matches.get(0))
                .getIngredient();
    }
}
