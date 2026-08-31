package io.everyonecodes.deliciousnessness.controller;

import io.everyonecodes.deliciousnessness.dto.IngredientSuggestionDto;
import io.everyonecodes.deliciousnessness.service.IngredientService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/ingredients")
public class IngredientController {

    private final IngredientService ingredientService;

    public IngredientController(IngredientService ingredientService){
        this.ingredientService = ingredientService;
    }

    @GetMapping("/autocomplete")
    public List<IngredientSuggestionDto> autocomplete(@RequestParam String q){
        return ingredientService.autocomplete(q);
    }
}
