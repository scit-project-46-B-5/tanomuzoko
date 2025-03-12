package org.scit.project.recipe.controller;

import org.scit.project.recipe.dto.RecipeConditionDTO;
import org.scit.project.recipe.dto.RecipeUserRequestDTO;
import org.scit.project.recipe.dto.RecipeUserResponseDTO;
import org.scit.project.recipe.service.RecipeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class RecipeController {

    private final RecipeService recipeService;

    @PostMapping("/recipe/chatGPT")
    public String showExample(@RequestBody RecipeUserRequestDTO recipeUserRequestDTO, HttpSession session) {

        //RecipeUserResponseDTO response = recipeService.getRecipeResponse(recipeUserRequestDTO);

        String title = "가지 꽁치 굴소스 볶음";
        String[] ingredients = {"가지 1개", "꽁치 1마리", "굴소스 2큰술", "곶감 1개"};
        String[] cookingMethods = {"가지를 채 썰어 물기를 제거한다.", 
                                    "꽁치는 소금을 뿌려 10분간 절인 후 물에 헹궈내어 물기를 제거한다.",
                                    "팬에 식용유를 두르고 가지를 볶다가 꽁치를 넣고 함께 볶는다.",
                                    "곶감과 굴소스를 넣고 약불에서 볶아 익힌다.",
                                    "접시에 담아 완성한다."};
        String usage = "일반식";
        String menu = "한식";
        String taste = "매운맛";
        String level = "초보";
        RecipeUserResponseDTO response = RecipeUserResponseDTO.TODTO(title, ingredients, cookingMethods, RecipeConditionDTO.TODTO(usage, menu, taste, level));
        
        session.setAttribute("recipe", response);

        return "redirect:/recipe/recommend/output";
    }

    @GetMapping("/recipe/recommend/output")
    public String viewRecipeRecoomendOutput(Model model, HttpSession session) {
        RecipeUserResponseDTO recipeResponse = session.getAttribute("recipe") != null ? (RecipeUserResponseDTO) session.getAttribute("recipe") : RecipeUserResponseDTO.empty();
        model.addAttribute("recipe", recipeResponse);

        return "recipe/recommend_result";
    }

    @GetMapping("/recipe/recommend")
    public String viewRecipeRecoomend(HttpSession session) {
        session.removeAttribute("recipe");
        return "recipe/recommend";
    }

}
