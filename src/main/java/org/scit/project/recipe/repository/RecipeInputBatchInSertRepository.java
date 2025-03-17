package org.scit.project.recipe.repository;

import java.util.List;

import org.scit.project.recipe.entity.RecipeInputKeywordEntity;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Repository
public class RecipeInputBatchInSertRepository {

    @PersistenceContext
    EntityManager entityManager;

    public void bulkInsertRecipeKeywords(List<RecipeInputKeywordEntity> keywords) {
        for (int i = 0; i < keywords.size(); i++) {
            entityManager.persist(keywords.get(i));
        }
        entityManager.flush(); // Final flush
        entityManager.clear();
    }

}
