package org.scit.project.recipe.controller;

import org.scit.project.recipe.dto.RecipeHistroyRequsetDTO;
import org.scit.project.recipe.service.RecipeHistoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class RecipeHistoryController {

    private final RecipeHistoryService recipeHistoryService;
    
    @PostMapping("/recipe/history/save")
    @ResponseBody
    public void saveRecipeHistory(@RequestBody RecipeHistroyRequsetDTO recipeHistroyRequsetDTO) throws InterruptedException {

        recipeHistoryService.saveRecipeHisotry(recipeHistroyRequsetDTO);
    }
}
