package org.scit.project.mypage.service;

import java.util.List;

import org.scit.project.mypage.dto.RecipeMyPageResponse;
import org.scit.project.mypage.dto.RecipeWrittenResponse;
import org.scit.project.mypage.repository.RecipeMyPageRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecipeMyPageService {

    private final RecipeMyPageRepository recipeMyPageRepository;

    public Page<RecipeMyPageResponse> findAllRecipeByUser(Long userSeq, int currentPage) {
        Pageable pageable = PageRequest.of(currentPage,  10, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Long> recipeIds = recipeMyPageRepository.findRecipeIdsByUser(userSeq, pageable);
        List<RecipeMyPageResponse> recipes = recipeMyPageRepository.findRecipesByIds(recipeIds.getContent()).stream().map(RecipeMyPageResponse::toDTO).toList();

        return new PageImpl<>(recipes, pageable, recipeIds.getTotalElements());
    };

    public List<RecipeWrittenResponse> findAllRecipeUsedAndBoardWrittenByLoginUser(Long userSeq) {
        
        return recipeMyPageRepository.findRecipeAndBoardWritten(userSeq).stream().map(RecipeWrittenResponse::TODTO).toList();
    }
    
    
}
