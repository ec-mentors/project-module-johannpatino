package io.everyonecodes.deliciousnessness.service;

import io.everyonecodes.deliciousnessness.dto.CategoryDto;
import io.everyonecodes.deliciousnessness.model.Category;
import io.everyonecodes.deliciousnessness.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public Category findOrCreate(String name) {
        String normalised = name.trim().toLowerCase();

        return categoryRepository.findByNameIgnoreCase(normalised)
                .orElseGet(() -> categoryRepository.save(new Category(normalised)));
    }

    @Transactional(readOnly = true)
    public List<CategoryDto> findAll() {
        return categoryRepository.findAllByOrderByNameAsc();
    }

    @Transactional
    public Set<Category> findOrCreateAll(Set<String> names) {
        Set<Category> categories = new LinkedHashSet<>();

        if (names == null) {
            return categories;
        }

        for (String name : names) {
            categories.add(findOrCreate(name));
        }
        return categories;
    }
}
