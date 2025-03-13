package org.scit.project.board.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.scit.project.board.dto.BoardDTO;
import org.scit.project.board.entity.BoardEntity;
import org.scit.project.board.entity.BoardImageEntity;
import org.scit.project.board.repository.BoardRepository;
import org.scit.project.board.repository.BoardImageRepository;
import org.scit.project.board_heart.repository.BoardHeartRepository;
import org.scit.project.reply.entity.ReplyEntity;
import org.scit.project.user.dto.LoginUserDetails;
import org.scit.project.user.entity.UserEntity;
import org.scit.project.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final BoardImageRepository boardImageRepository;
    private final BoardHeartRepository boardHeartRepository;

    @Value("${spring.servlet.multipart.location}")
    private String uploadPath;

    @PersistenceContext
    private EntityManager entityManager;

    public void insertBoard(BoardDTO boardDTO) {
        LoginUserDetails loginUser = (LoginUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = loginUser.getUserId();
        UserEntity user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("no such user"));

        BoardEntity entity = BoardEntity.toEntity(boardDTO, user);
        BoardEntity savedBoard = boardRepository.save(entity);

        if (boardDTO.getThumbnailUrl() != null && !boardDTO.getThumbnailUrl().isEmpty()) {
            String thumbnailUrl = boardDTO.getThumbnailUrl();
            String savedFileName = "";

            if (thumbnailUrl.startsWith("/uploads/")) {
                savedFileName = thumbnailUrl.substring(9);
            } else {
                String newUUID = UUID.randomUUID().toString();
                String ext = "";
                if (thumbnailUrl.startsWith("data:image/")) {
                    int slashIndex = thumbnailUrl.indexOf("/");
                    int semicolonIndex = thumbnailUrl.indexOf(";");
                    if (slashIndex != -1 && semicolonIndex != -1) {
                        ext = "." + thumbnailUrl.substring(slashIndex + 1, semicolonIndex);
                    }
                }
                savedFileName = "thumbnail_" + newUUID + ext;
            }

            BoardImageEntity imageEntity = BoardImageEntity.builder()
                .boardEntity(savedBoard)
                .originalFileName(boardDTO.getThumbnail())
                .savedFileName(savedFileName)
                .build();
            boardImageRepository.save(imageEntity);
        }
    }

    @Transactional
    public BoardDTO selectOne(Long boardSeq) {
        boardRepository.incrementHitCount(boardSeq);
        BoardEntity boardEntity = boardRepository.findById(boardSeq).orElse(null);
        if (boardEntity == null) {
            return null;
        }
        return BoardDTO.toDTO(boardEntity);
    }

    @Transactional
    public List<BoardDTO> getRecentPostsByUser(Long userSeq, Long currentBoardSeq) {
        UserEntity user = userRepository.findById(userSeq)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<BoardEntity> boardEntities = boardRepository.findTop10ByUserEntityOrderByCreateDateDesc(user)
                .stream()
                .filter(board -> !board.getBoardSeq().equals(currentBoardSeq))
                .limit(10)
                .collect(Collectors.toList());

        return boardEntities.stream()
                .map(BoardDTO::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<BoardDTO> getPopularPosts() {
        List<BoardEntity> allBoards = boardRepository.findAll();
        List<BoardDTO> popularPosts = allBoards.stream()
            .sorted((b1, b2) -> {
                int heartCount1 = boardHeartRepository.countByBoardAndIsHeartedTrue(b1);
                int heartCount2 = boardHeartRepository.countByBoardAndIsHeartedTrue(b2);
                return Integer.compare(heartCount2, heartCount1);
            })
            .limit(5)
            .map(BoardDTO::toDTO)
            .collect(Collectors.toList());
        return popularPosts;
    }

    public BoardDTO updateSelectOne(Long boardSeq) {
        Optional<BoardEntity> temp = boardRepository.findById(boardSeq);
        if (!temp.isPresent()) return null;
        return BoardDTO.toDTO(temp.get());
    }

    @Transactional
    public void updateBoard(BoardDTO boardDTO) {
        BoardEntity entity = boardRepository.findById(boardDTO.getBoardSeq())
                .orElseThrow(() -> new RuntimeException("게시글이 존재하지 않습니다."));
        entity.setBoardTitle(boardDTO.getBoardTitle());
        entity.setBoardContent(boardDTO.getBoardContent());
        boardRepository.save(entity);

        if (boardDTO.getThumbnail() != null && !boardDTO.getThumbnail().isEmpty()){
            Optional<BoardImageEntity> optImage = boardImageRepository.findByBoardEntity(entity);
            String thumbnailUrl = boardDTO.getThumbnailUrl();
            String savedFileName = "";
            if (thumbnailUrl.startsWith("/uploads/")){
                savedFileName = thumbnailUrl.substring(9);
            } else {
                String newUUID = UUID.randomUUID().toString();
                String ext = "";
                if (thumbnailUrl.startsWith("data:image/")){
                    int slashIndex = thumbnailUrl.indexOf("/");
                    int semicolonIndex = thumbnailUrl.indexOf(";");
                    if (slashIndex != -1 && semicolonIndex != -1){
                        ext = "." + thumbnailUrl.substring(slashIndex+1, semicolonIndex);
                    }
                }
                savedFileName = "thumbnail_" + newUUID + ext;
            }
            if (optImage.isPresent()){
                BoardImageEntity imageEntity = optImage.get();
                imageEntity.setOriginalFileName(boardDTO.getThumbnail());
                imageEntity.setSavedFileName(savedFileName);
                boardImageRepository.save(imageEntity);
            } else {
                BoardImageEntity imageEntity = BoardImageEntity.builder()
                        .boardEntity(entity)
                        .originalFileName(boardDTO.getThumbnail())
                        .savedFileName(savedFileName)
                        .build();
                boardImageRepository.save(imageEntity);
            }
        }
    }

    public void deleteBoard(Long boardSeq) {
        Optional<BoardEntity> boardOpt = boardRepository.findById(boardSeq);
        if (boardOpt.isEmpty()) {
            throw new IllegalArgumentException("게시물이 존재하지 않습니다.");
        }
        
        BoardEntity boardEntity = boardOpt.get();
        boardEntity.setIsDeleted(true);
        boardRepository.save(boardEntity);
    }

    public List<Map<String, Object>> findAllByUser(Long userSeq) {
        List<Object[]> results = boardRepository.findRecipesByUser(userSeq);
        List<Map<String, Object>> recipes = new ArrayList<>();
        for (Object[] row : results) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", row[0]);
            map.put("title", row[1]);
            recipes.add(map);
        }
        return recipes;
    }
    
    // recipe_output_content 테이블에서 recipe_seq에 해당하는 output_content 값을 조회
    public String getRecipeOutputContent(Long recipeSeq) {
        String sql = "SELECT output_content FROM recipe_output_content WHERE recipe_seq = :recipeSeq";
        List<?> list = entityManager.createNativeQuery(sql)
                    .setParameter("recipeSeq", recipeSeq)
                    .getResultList();
        if(list != null && !list.isEmpty()) {
            return list.get(0).toString();
        }
        return "";
    }
}
