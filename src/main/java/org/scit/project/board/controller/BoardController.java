package org.scit.project.board.controller;

import java.util.HashMap;
import java.util.Map;
import org.scit.project.board.dto.BoardDTO;
import org.scit.project.board.service.BoardService;
import org.scit.project.board.util.FileService;
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

        BoardDTO boardDTO = boardService.selectOne(boardSeq);
        model.addAttribute("board", boardDTO);
        return "/board/detail";
    }

    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadFile(@RequestParam("file") MultipartFile file) {
        String uploadPath = "";  // 실제 저장 경로로 변경
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
}
