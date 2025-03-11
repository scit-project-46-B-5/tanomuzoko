package org.scit.project.mypage.service;

import java.util.Collections;
import java.util.List;

import org.scit.project.mypage.dto.RecipeMyPageResponse;
import org.scit.project.mypage.repository.RecipeMyPageRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecipeMyPageService {

    private final RecipeMyPageRepository recipeMyPageRepository;


    public List<RecipeMyPageResponse> findAllByUser(Long userSeq) {
        return recipeMyPageRepository.findByUserEntity_UserSeqAndRecipeInputKeywordEntityListIsNotNullAndRecipeOutputEntityIsNotNull(userSeq)
                                        .stream().map(RecipeMyPageResponse::toDTO).toList();
         
    };
    
    
}
