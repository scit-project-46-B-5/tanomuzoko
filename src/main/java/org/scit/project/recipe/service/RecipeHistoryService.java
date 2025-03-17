package org.scit.project.recipe.service;

import java.util.List;
import java.util.stream.Collectors;

import org.scit.project.recipe.dto.RecipeHistroyRequsetDTO;
import org.scit.project.recipe.entity.RecipeEntity;
import org.scit.project.recipe.entity.RecipeInputKeywordEntity;
import org.scit.project.recipe.entity.RecipeOutputEntity;
import org.scit.project.recipe.repository.RecipeInputBatchInSertRepository;
import org.scit.project.recipe.repository.RecipeOutputRepository;
import org.scit.project.recipe.repository.RecipeRepository;
import org.scit.project.user.dto.LoginUserDetails;
import org.scit.project.user.entity.UserEntity;
import org.scit.project.user.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecipeHistoryService {
    
    private final UserRepository userRepository;
    private final RecipeOutputRepository recipeOutputRepository;
    private final RecipeRepository recipeRepository;
    private final RecipeInputBatchInSertRepository RecipeInputBatchInSertRepository;

    /**
     * recipeEntity - recipesInputKeyword - recipeOutput 순으로 save
     * 저장이 성공하면 저장 성공여부를 판별하는 recipeSeq를 return
     * @param recipeHistroyRequsetDTO
     * @return
     */
    @Transactional
    public Long saveRecipeAndReturnSavedPK(RecipeHistroyRequsetDTO recipeHistroyRequsetDTO) {
        UserEntity user = findUser();
        
        RecipeEntity recipeEntity = recipeRepository.save(RecipeEntity.TOENTITY(user));

        if (recipeEntity == null) {
            throw new RuntimeException("recipe is not saved due to error");
        }

        List<RecipeInputKeywordEntity> recipeInputKeywords = recipeHistroyRequsetDTO.getAllConditions()
                                                                                                    .stream()
                                                                                                    .map(condition -> RecipeInputKeywordEntity.TOENTITY(recipeEntity, condition))
                                                                                                    .collect(Collectors.toList());

        // 여기서 save query 안 나가고 select query가 발생함. 불필요한.. @Id에 generated 전략이 IDENTITY가 아니면 entity를 check하기 때문이라고 함.
        RecipeInputBatchInSertRepository.bulkInsertRecipeKeywords(recipeInputKeywords);
        
        recipeOutputRepository.save(RecipeOutputEntity.TOENTTIY(
                                                                recipeEntity, 
                                                                recipeHistroyRequsetDTO.getTitle(), 
                                                                recipeHistroyRequsetDTO.getOutputContent()
                                                            )
                                    );
            
        return recipeEntity.getRecipeSeq();
    }


    private UserEntity findUser() {
        LoginUserDetails loginUser = (LoginUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = loginUser.getUserId();
        return userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("no such user, using in saveRecipeAndReturnSavedPK"));
    }
}
