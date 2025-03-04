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
import org.scit.project.mainpage.dto.BoardWithHeartCountDTO;
import org.scit.project.mainpage.dto.MainDTO;
import org.scit.project.mainpage.repository.MainRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class MainService {

    private final MainRepository mainRepository;
    private final BoardHeartRepository boardHeartRepository;
    private final BoardImageRepository boardImageRepository;

    public List<MainDTO> getPosts(int page) {
        int pageSize = 10;

        Page<BoardWithHeartCountDTO> temp = mainRepository.findAllWithHeartCount(PageRequest.of(page, pageSize));

        return temp.stream()
                .map(dto -> {
                    Optional<BoardImageEntity> imageOpt = boardImageRepository.findByBoardEntity(dto.getBoard());
                    String boardImageOriginalFileName = imageOpt.map(BoardImageEntity::getOriginalFileName).orElse("");
                    return MainDTO.toDTO(dto.getBoard(), dto.getHeartCount(), boardImageOriginalFileName);
                })
                .collect(Collectors.toList());
    }

    public List<MainDTO> getTop5LikedPostsByPeriod(String period) {
        LocalDateTime startDate = period.equals("monthly")
                ? LocalDateTime.now().minusMonths(1)
                : LocalDateTime.now().minusWeeks(1);

        List<BoardWithHeartCountDTO> topPosts = mainRepository.findTopPostsByPeriodAndHeartCount(startDate, PageRequest.of(0, 5));
        
        return topPosts.stream()
                .map(dto -> {
                    Optional<BoardImageEntity> imageOpt = boardImageRepository.findByBoardEntity(dto.getBoard());
                    String boardImageOriginalFileName = imageOpt.map(BoardImageEntity::getOriginalFileName).orElse("");
                    return MainDTO.toDTO(dto.getBoard(), dto.getHeartCount(), boardImageOriginalFileName);
                })
                .collect(Collectors.toList());
    }
    
    public boolean isLastPage(int page) {
        int pageSize = 10;
        Page<BoardEntity> nextPage = mainRepository.findAll(PageRequest.of(page + 1, pageSize));
        return !nextPage.hasContent();
    }

    public List<MainDTO> getTop3LikedPosts() {
        List<BoardWithHeartCountDTO> topPosts = boardHeartRepository.findTop3LikedBoards(PageRequest.of(0, 3));

        return topPosts.stream()
                .map(dto -> {
                    Optional<BoardImageEntity> imageOpt = boardImageRepository.findByBoardEntity(dto.getBoard());
                    String boardImageOriginalFileName = imageOpt.map(BoardImageEntity::getOriginalFileName).orElse("");
                    return MainDTO.toDTO(dto.getBoard(), dto.getHeartCount(), boardImageOriginalFileName);
                })
                .collect(Collectors.toList());
    }
}
