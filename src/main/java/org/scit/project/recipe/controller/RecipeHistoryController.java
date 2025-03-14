package org.scit.project.recipe.controller;

import org.scit.project.recipe.dto.RecipeHistroyRequsetDTO;
import org.scit.project.recipe.service.RecipeHistoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class RecipeHistoryController {

    private final RecipeHistoryService recipeHistoryService;
    
    @PostMapping("/recipe/history/save")
    @ResponseBody
    public ResponseEntity<Long> saveRecipeHistory(@RequestBody RecipeHistroyRequsetDTO recipeHistroyRequsetDTO, HttpSession session) throws InterruptedException {
        String userNonce = (String) session.getAttribute("nonce");

        if (!StringUtils.hasText(userNonce) || !recipeHistroyRequsetDTO.getNonce().equals(userNonce)) {
            throw new RuntimeException("nonce 값이 존재하지 않습니다.");
        }

        Long savedRecipeSeq = recipeHistoryService.saveRecipeAndReturnSavedPK(recipeHistroyRequsetDTO);
        if(savedRecipeSeq != 0) {
            session.removeAttribute("nonce");   
        }
        
        return ResponseEntity.ok(savedRecipeSeq);
    }
}
