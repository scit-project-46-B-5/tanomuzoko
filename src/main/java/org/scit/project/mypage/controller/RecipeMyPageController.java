package org.scit.project.mypage.controller;

import org.scit.project.mypage.dto.PageDTO;
import org.scit.project.mypage.dto.RecipeMyPageResponse;
import org.scit.project.mypage.service.RecipeMyPageService;
import org.scit.project.user.dto.LoginUserDetails;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class RecipeMyPageController {

    private final RecipeMyPageService recipeMyPageService;


    @GetMapping("/mypage/getRecipeSave")
    @ResponseBody
    public PageDTO<RecipeMyPageResponse> recipeSave(Model model, @AuthenticationPrincipal LoginUserDetails loginUserDetails, 
                                        @RequestParam(name = "page", required = false) int page) {
        Page<RecipeMyPageResponse> recipePage = recipeMyPageService.findAllRecipeByUser(loginUserDetails.getUserSeq(), page);
        PageDTO<RecipeMyPageResponse> recipes = PageDTO.TODTO(recipePage);

        return recipes; 
    }


    @GetMapping("/mypage/recipeSave")
    public String viewRecipeSave() {

        return "mypage/recipeSave_mypage";
    }


    
}
