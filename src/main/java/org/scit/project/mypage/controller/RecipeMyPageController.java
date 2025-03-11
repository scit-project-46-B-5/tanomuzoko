package org.scit.project.mypage.controller;

import org.scit.project.mypage.service.RecipeMyPageService;
import org.scit.project.recipe.service.RecipeService;
import org.scit.project.user.dto.LoginUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class RecipeMyPageController {

    private final RecipeMyPageService recipeMyPageService;

    @GetMapping("/mypage/recipeSave")
    public String recipeSave(Model model, @AuthenticationPrincipal LoginUserDetails loginUserDetails) {
        model.addAttribute("recipes", recipeMyPageService.findAllByUser(loginUserDetails.getUserSeq()));
        return "mypage/recipeSave_mypage"; 
    }


    
}
