package org.scit.project.recipe.controller;

import org.scit.project.recipe.dto.RecipeUserRequestDTO;
import org.scit.project.recipe.service.RecipeService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class RecipeController {

    private final RecipeService recipeService;

    // @GetMapping({"/",""})
    // public String index() {

    // return new String("index");
    // }

    @PostMapping("/recipe/chatGPT")
    public String showExample(@ModelAttribute RecipeUserRequestDTO recipeUserRequestDTO, RedirectAttributes redirectAttributes) {

        recipeService.sendRequestToChatGPT(recipeUserRequestDTO);
        redirectAttributes.addFlashAttribute("recipe", recipeUserRequestDTO);
        return "redirect:/recipe/recommend/output";

    }

    @GetMapping("/recipe/recommend/output")
    public String viewRecipeRecoomendOutput() {
        return "recipe/recommend_result";
    }

    @GetMapping("/recipe/recommend")
    public String viewRecipeRecoomend() {
        return "recipe/recommend";
    }

}
