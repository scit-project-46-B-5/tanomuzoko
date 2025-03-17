package org.scit.project.mypage.service;

import java.util.List;

import org.scit.project.mypage.dto.RecipeMyPageResponse;
import org.scit.project.mypage.repository.RecipeMyPageRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecipeMyPageService {

    private final RecipeMyPageRepository recipeMyPageRepository;

    public Page<RecipeMyPageResponse> findAllRecipeByUser(Long userSeq, int currentPage) {
        Pageable pageable = PageRequest.of(currentPage, 3, Sort.by(Sort.Direction.ASC, "createdAt"));
        
        List<Long> recipeIds = recipeMyPageRepository.findRecipeIdsByUser(userSeq);
        Page<RecipeMyPageResponse> recipes = recipeMyPageRepository.findRecipesByIds(recipeIds, pageable).map(RecipeMyPageResponse::toDTO);

        return recipes;
    };
    
    
}
