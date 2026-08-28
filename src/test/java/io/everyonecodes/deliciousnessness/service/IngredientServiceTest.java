package io.everyonecodes.deliciousnessness.service;

import io.everyonecodes.deliciousnessness.model.Ingredient;
import io.everyonecodes.deliciousnessness.model.IngredientName;
import io.everyonecodes.deliciousnessness.model.Language;
import io.everyonecodes.deliciousnessness.repository.IngredientNameRepository;
import io.everyonecodes.deliciousnessness.repository.IngredientRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IngredientServiceTest {

    @Mock
    private IngredientRepository ingredientRepository;

    @Mock
    private IngredientNameRepository ingredientNameRepository;

    @InjectMocks
    private IngredientService ingredientService;

    @Test
    void existingName_reusesIngredientAndCreatesNothing() {
        Ingredient onion = new Ingredient("onion");
        IngredientName polishName = new IngredientName(onion, "cebula", Language.PL);

        when(ingredientNameRepository.findByNameIgnoreCase("cebula"))
                .thenReturn(List.of(polishName));

        Ingredient result = ingredientService.findOrCreate("   Cebula  ", Language.PL);

        assertThat(result).isSameAs(onion);
        verify(ingredientRepository, never()).save(any());
        verify(ingredientNameRepository, never()).save(any());
    }

    @Test
    void unknownName_createsBothIngredientAndName() {
        when(ingredientNameRepository.findByNameIgnoreCase("kasza gryczana"))
                .thenReturn(List.of());
        when(ingredientRepository.save(any(Ingredient.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Ingredient result = ingredientService.findOrCreate("Kasza Gryczana", Language.PL);

        assertThat(result.getCanonicalName()).isEqualTo("kasza gryczana");

        ArgumentCaptor<IngredientName> saved = ArgumentCaptor.forClass(IngredientName.class);
        verify(ingredientNameRepository).save(saved.capture());

        assertThat(saved.getValue().getName()).isEqualTo("kasza gryczana");
        assertThat(saved.getValue().getLanguageCode()).isEqualTo(Language.PL);

    }
}