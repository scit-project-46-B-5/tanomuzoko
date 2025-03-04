package org.scit.project.mypage.entity;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
@Table(name = "board")
public class MyBoardEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_seq")
    private Long boardSeq;

    @Column(name = "user_seq")
    private Long userSeq;

    @Column(name = "board_title", length = 100, nullable = false)
    private String boardTitle;

    @Column(name = "board_content", length = 4000)
    private String boardContent;

    @Column(name = "hit_count")
    private int hitCount;

    @Column(name = "create_date")
    @CreationTimestamp
    private LocalDateTime createDate;

    @Column(name = "update_date")
    @UpdateTimestamp
    private LocalDateTime updateDate;

    @Column(name = "is_deleted")
    private boolean isDeleted;

    @OneToMany(mappedBy = "myBoardEntity", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MyBoardImageEntity> images;

    @OneToMany(mappedBy = "myBoardEntity", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MyBoardHeartEntity> hearts;
}
