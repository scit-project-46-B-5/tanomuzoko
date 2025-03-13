package org.scit.project.recipe.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.scit.project.user.entity.UserEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "recipe")
public class RecipeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recipe_seq")
    private Long recipeSeq;

    @ManyToOne
    @JoinColumn(name = "user_seq", referencedColumnName = "user_seq")
    private UserEntity userEntity;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "recipeEntity", cascade = CascadeType.REMOVE)
    @Builder.Default
    private List<RecipeInputKeywordEntity> recipeInputKeywordEntityList = new ArrayList<>();

    @OneToOne(mappedBy = "recipeEntity", cascade = CascadeType.REMOVE)
    private RecipeOutputEntity recipeOutputEntity;

    public static RecipeEntity TOENTITY(UserEntity userEntity) {
        return RecipeEntity.builder()
                            .userEntity(userEntity)
                            .build();
    }
}
