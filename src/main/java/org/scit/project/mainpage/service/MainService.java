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

    /**
     * 전체 게시글을 페이징하여 가져오는 메서드
     * 
     * @param page 현재 페이지 번호
     * @return 게시글 리스트 (MainDTO)
     */
    public List<MainDTO> getPosts(int page) {
        int pageSize = 10; // 한 페이지에 10개의 게시글을 가져옴

        // 공감(좋아요) 개수를 포함한 게시글 목록을 가져옴
        Page<BoardWithHeartCountDTO> temp = mainRepository.findAllWithHeartCount(PageRequest.of(page, pageSize));

        // 게시글 데이터를 DTO로 변환하여 반환
        return temp.stream()
                .map(dto -> {
                    // 해당 게시글의 대표 이미지 가져오기 (없으면 빈 문자열 반환)
                    Optional<BoardImageEntity> imageOpt = boardImageRepository.findByBoardEntity(dto.getBoard());
                    String boardImageOriginalFileName = imageOpt.map(BoardImageEntity::getOriginalFileName).orElse("");

                    // DTO 변환 후 반환
                    return MainDTO.toDTO(dto.getBoard(), dto.getHeartCount(), boardImageOriginalFileName);
                })
                .collect(Collectors.toList());
    }

    /**
     * 기간별(주간/월간) 공감(좋아요) 수가 가장 많은 게시글 TOP5 조회
     * 
     * @param period "weekly" 또는 "monthly" (기간 설정)
     * @return 공감 수 기준으로 정렬된 상위 5개 게시글 리스트
     */
    public List<MainDTO> getTop5LikedPostsByPeriod(String period) {
        // 기간에 따라 조회 시작 날짜 설정
        LocalDateTime startDate = period.equals("monthly")
                ? LocalDateTime.now().minusMonths(1) // 한 달 전
                : LocalDateTime.now().minusWeeks(1); // 일주일 전

        // 지정된 기간 내에서 공감 수가 많은 게시글 5개 조회
        List<BoardWithHeartCountDTO> topPosts = mainRepository.findTopPostsByPeriodAndHeartCount(startDate,
                PageRequest.of(0, 5));

        return topPosts.stream()
                .map(dto -> {
                    Optional<BoardImageEntity> imageOpt = boardImageRepository.findByBoardEntity(dto.getBoard());
                    String boardImageOriginalFileName = imageOpt.map(BoardImageEntity::getOriginalFileName).orElse("");
                    return MainDTO.toDTO(dto.getBoard(), dto.getHeartCount(), boardImageOriginalFileName);
                })
                .collect(Collectors.toList());
    }
    
    /**
     * 현재 페이지가 마지막 페이지인지 확인하는 메서드
     * 
     * @param page 현재 페이지 번호
     * @return 마지막 페이지 여부 (true = 마지막 페이지, false = 다음 페이지 존재)
     */
    public boolean isLastPage(int page) {
        int pageSize = 10;
        // 다음 페이지를 조회하여 내용이 없는지 확인
        Page<BoardEntity> nextPage = mainRepository.findAll(PageRequest.of(page + 1, pageSize));
        return !nextPage.hasContent(); // 다음 페이지가 없으면 true 반환
    }

    /**
     * 전체 기간 중 공감(좋아요) 수가 가장 많은 게시글 TOP3 조회
     * 
     * @return 공감 수 기준으로 정렬된 상위 3개 게시글 리스트
     */
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
