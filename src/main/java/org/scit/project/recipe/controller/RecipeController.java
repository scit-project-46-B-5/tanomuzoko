package org.scit.project.recipe.controller;

import org.scit.project.recipe.dto.RecipeUserRequestDTO;
import org.scit.project.recipe.service.RecipeService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class RecipeController {

    private final RecipeService recipeService;

    // @GetMapping({"/",""})
    // public String index() {

    // return new String("index");
    // }

    @GetMapping("/recipe/chatGPT")
    @ResponseBody
    public String showExample(RecipeUserRequestDTO recipeUserRequestDTO) {
        return recipeService.sendRequestToChatGPT(recipeUserRequestDTO);

    }

    @GetMapping("/recipe/recommend")
    public String viewRecipeRecoomend() {
        return "recipe/recommend";
    }

}
