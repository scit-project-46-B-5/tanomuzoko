package org.scit.project.mainpage.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.scit.project.board.entity.BoardEntity;
import org.scit.project.board.entity.BoardImageEntity;
import org.scit.project.board.repository.BoardImageRepository;
import org.scit.project.board_heart.repository.BoardHeartRepository;
import org.scit.project.mainpage.dto.MainDTO;
import org.scit.project.mainpage.repository.MainRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class MainService {

    private final MainRepository mainRepository;
    private final BoardHeartRepository boardHeartRepository;
    private final BoardImageRepository boardImageRepository;

    public List<MainDTO> getPosts(int page) {
        int pageSize = 10;
        Page<BoardEntity> temp = mainRepository.findAll(PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "createDate")));
        List<MainDTO> list = new ArrayList<>();

        temp.forEach(entity -> {
            int heartCount = boardHeartRepository.countByBoardAndIsHeartedTrue(entity);
            Optional<BoardImageEntity> imageOpt = boardImageRepository.findByBoardEntity(entity);
            String boardImageOriginalFileName = imageOpt.isPresent() ? imageOpt.get().getOriginalFileName() : "";
            list.add(MainDTO.toDTO(entity, heartCount, boardImageOriginalFileName));
        });
        return list;
    }

    public List<MainDTO> getTopPosts(String period) {
        LocalDateTime startDate = period.equals("monthly")
                ? LocalDateTime.now().minusMonths(1)
                : LocalDateTime.now().minusWeeks(1);

        List<BoardEntity> topPosts = mainRepository.findTopPostsByHeartCount(startDate, PageRequest.of(0, 5));
        return topPosts.stream()
                .map(entity -> {
                    int heartCount = boardHeartRepository.countByBoardAndIsHeartedTrue(entity);
                    Optional<BoardImageEntity> imageOpt = boardImageRepository.findByBoardEntity(entity);
                    String boardImageOriginalFileName = imageOpt.isPresent() ? imageOpt.get().getOriginalFileName() : "";
                    return MainDTO.toDTO(entity, heartCount, boardImageOriginalFileName);
                })
                .collect(Collectors.toList());
    }
    
    public boolean isLastPage(int page) {
        int pageSize = 10;
        Page<BoardEntity> nextPage = mainRepository.findAll(PageRequest.of(page + 1, pageSize));
        return !nextPage.hasContent();
    }

    public List<MainDTO> getTopLikedPosts() {
        List<Object[]> results = boardHeartRepository.findTopLikedBoards(PageRequest.of(0, 3));
        List<MainDTO> topPosts = new ArrayList<>();

        for (Object[] result : results) {
            Long boardSeq = (Long) result[0];
            int heartCount = ((Number) result[1]).intValue();
            Optional<BoardEntity> boardEntityOpt = mainRepository.findById(boardSeq);
            boardEntityOpt.ifPresent(boardEntity -> {
                Optional<BoardImageEntity> imageOpt = boardImageRepository.findByBoardEntity(boardEntity);
                String boardImageOriginalFileName = imageOpt.isPresent() ? imageOpt.get().getOriginalFileName() : "";
                topPosts.add(MainDTO.toDTO(boardEntity, heartCount, boardImageOriginalFileName));
            });
        }
        return topPosts;
    }
}
