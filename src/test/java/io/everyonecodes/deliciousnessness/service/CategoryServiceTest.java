package io.everyonecodes.deliciousnessness.service;

import io.everyonecodes.deliciousnessness.model.Category;
import io.everyonecodes.deliciousnessness.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @Test
    void existingName_resusesCategoryAndCreatesNothing() {
        Category soup = new Category("soup");

        when(categoryRepository.findByNameIgnoreCase("soup"))
                .thenReturn(Optional.of(soup));

        Category result = categoryService.findOrCreate("   Soup  ");

        assertThat(result).isSameAs(soup);
        verify(categoryRepository, never()).save(any());
    }

    @Test
    void unknownName_createsCategoryWithNormalisedName() {
        when(categoryRepository.findByNameIgnoreCase("dessert"))
                .thenReturn(Optional.empty());
        when(categoryRepository.save(any(Category.class)))
                .then(invocation -> invocation.getArgument(0));

        Category result = categoryService.findOrCreate("Dessert");

        assertThat(result.getName()).isEqualTo("dessert");
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    void nullNames_returnEmptySetAndTouchesNothing() {
        Set<Category> result = categoryService.findOrCreateAll(null);

        assertThat(result).isEmpty();
        verify(categoryRepository, never()).findByNameIgnoreCase(any());
        verify(categoryRepository, never()).save(any());
    }
}