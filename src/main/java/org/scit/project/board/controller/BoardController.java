package org.scit.project.board.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.scit.project.board.dto.BoardDTO;
import org.scit.project.board.service.BoardService;
import org.scit.project.board.util.FileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    
    // 실제 저장 경로 (application.properties에서 spring.servlet.multipart.location에 설정한 경로)
    @Value("${spring.servlet.multipart.location}")
    private String uploadPath;

    @GetMapping("/board")
    public String board() {
        return "/board/board";
    }

    @GetMapping("/boardWrite")
    public String boardWrite() {
        return "/board/boardWrite";
    }

    @PostMapping("/boardWrite")
    public String boardWrite(@ModelAttribute BoardDTO boardDTO) {
        boardService.insertBoard(boardDTO);
        return "redirect:/";
    }

    @GetMapping("/boardDetail")
    public String boardDetail(@RequestParam(name = "boardSeq") Long boardSeq,
            @AuthenticationPrincipal UserDetails userDetails,
            Model model) {

        // 조회수는 별도의 bulk update로 처리하여 update_date가 변경되지 않도록 함
        BoardDTO boardDTO = boardService.selectOne(boardSeq);
        // 작성자(userSeq)에 해당하는 최신 게시물 10개 조회 (게시글 작성자 기준)
        List<BoardDTO> recentPosts = boardService.getRecentPostsByUser(boardDTO.getUserSeq());
        // 인기 게시글 5개 조회 (hitCount 기준 내림차순)
        List<BoardDTO> popularPosts = boardService.getPopularPosts();
        model.addAttribute("board", boardDTO);
        model.addAttribute("recentPosts", recentPosts);
        model.addAttribute("popularPosts", popularPosts);
        return "/board/detail";
    }

    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadFile(@RequestParam("file") MultipartFile file) {
        String savedFileName = FileService.saveFile(file, uploadPath);

        if (savedFileName == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "파일 업로드 실패"));
        }

        String fileUrl = "/uploads/" + savedFileName;  // 클라이언트에서 접근할 URL
        Map<String, String> response = new HashMap<>();
        response.put("fileUrl", fileUrl);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/deleteFile")
    public ResponseEntity<Map<String, String>> deleteFile(@RequestParam("fileUrl") String fileUrl) {
        String savedFileName = "";
        // 파일 URL이 "/uploads/..." 형태라면 이를 제거하여 저장된 파일 이름 추출
        if(fileUrl.startsWith("/uploads/")) {
            savedFileName = fileUrl.substring(9);
        } else {
            savedFileName = fileUrl;
        }
        String fullPath = uploadPath + "/" + savedFileName;
        boolean result = FileService.deleteFile(fullPath);
        if(result){
            return ResponseEntity.ok(Map.of("message", "삭제 성공"));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "파일 삭제 실패"));
        }
    }
}
