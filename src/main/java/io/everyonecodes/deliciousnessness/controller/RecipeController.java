package io.everyonecodes.deliciousnessness.controller;

import io.everyonecodes.deliciousnessness.dto.CreateRecipeRequest;
import io.everyonecodes.deliciousnessness.dto.RecipeDto;
import io.everyonecodes.deliciousnessness.dto.RecipeSummaryDto;
import io.everyonecodes.deliciousnessness.model.Recipe;
import io.everyonecodes.deliciousnessness.service.RecipeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recipes")
public class RecipeController {

    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping
    public List<RecipeSummaryDto> findAll() {
        return recipeService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecipeDto> findById(@PathVariable Long id) {
        return recipeService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RecipeDto create(@RequestBody CreateRecipeRequest request) {
        return recipeService.create(request);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RecipeDto> update(@PathVariable Long id,
                                            @RequestBody CreateRecipeRequest request) {
        return recipeService.update(id, request)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return recipeService.deleteById(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
