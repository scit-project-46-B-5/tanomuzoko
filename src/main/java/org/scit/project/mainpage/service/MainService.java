package org.scit.project.mainpage.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.scit.project.board.entity.BoardEntity;
import org.scit.project.board_heart.repository.BoardHeartRepository;
import org.scit.project.mainpage.dto.BoardWithHeartCountDTO;
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

    public List<MainDTO> getPosts(int page) {

        int pageSize = 10;

        Page<BoardWithHeartCountDTO> temp = mainRepository.findAllWithHeartCount(PageRequest.of(page, pageSize));

        return temp.stream()
                .map(dto -> MainDTO.toDTO(dto.getBoard(), dto.getHeartCount()))
                .collect(Collectors.toList());
    }

    public List<MainDTO> getTop5LikedPostsByPeriod(String period) {
        LocalDateTime startDate = period.equals("monthly")
                ? LocalDateTime.now().minusMonths(1)
                : LocalDateTime.now().minusWeeks(1);

        List<BoardWithHeartCountDTO> topPosts = mainRepository.findTopPostsByPeriodAndHeartCount(startDate, PageRequest.of(0, 5));
        
        return topPosts.stream()
                .map(dto -> MainDTO.toDTO(dto.getBoard(), dto.getHeartCount()))
                .collect(Collectors.toList());
    }
    
    public boolean isLastPage(int page) {
        int pageSize = 10;
        Page<BoardEntity> nextPage = mainRepository.findAll(PageRequest.of(page + 1, pageSize));
        return !nextPage.hasContent(); // 다음 페이지에 데이터가 없으면 마지막 페이지
    }

    public List<MainDTO> getTopLikedPosts() {
        List<Object[]> results = boardHeartRepository.findTopLikedBoards(PageRequest.of(0, 3));

        List<MainDTO> topPosts = new ArrayList<>();

        for (Object[] result : results) {
            Long boardSeq = (Long) result[0];
            int heartCount = ((Number) result[1]).intValue();

            Optional<BoardEntity> boardEntityOpt = mainRepository.findById(boardSeq);
            boardEntityOpt.ifPresent(boardEntity -> topPosts.add(MainDTO.toDTO(boardEntity, heartCount)));
        }

        return topPosts;
    }
}
