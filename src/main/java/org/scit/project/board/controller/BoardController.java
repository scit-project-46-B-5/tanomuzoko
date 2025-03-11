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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @Value("${spring.servlet.multipart.location}")
    private String uploadPath;

    @GetMapping("/board")
    public String board() {
    	
        return "board/board";
    }

    @GetMapping("/boardWrite")
    public String boardWrite() {
        return "board/boardWrite";
    }

    @PostMapping("/boardWrite")
    public String boardWrite(@ModelAttribute BoardDTO boardDTO) {
        boardService.insertBoard(boardDTO);
        return "redirect:/";
    }
    
    @GetMapping("/boardUpdate")
    public String boardUpdate(
    		@RequestParam(name="boardSeq") Long boardSeq
    		, Model model) {
    	
    	BoardDTO board = boardService.updateSelectOne(boardSeq);  	
    	model.addAttribute("board", board);
    	
    	return "board/boardUpdate";
    }
    
    @PostMapping("/boardUpdate")
    public String boardUpdate(@ModelAttribute BoardDTO boardDTO) {
        boardService.updateBoard(boardDTO);
        
        return "redirect:/";
    }
    
    
    @PostMapping("/boardDelete")
    public String boardDelete(@RequestParam(name="boardSeq") Long boardSeq) {
    	boardService.deleteBoard(boardSeq);
    	
    	return "board/board";
    }

    @GetMapping("/boardDetail")
    public String boardDetail(@RequestParam(name = "boardSeq") Long boardSeq,
            @AuthenticationPrincipal UserDetails userDetails,
            Model model) {

        BoardDTO boardDTO = boardService.selectOne(boardSeq);
        
        List<BoardDTO> recentPosts = boardService.getRecentPostsByUser(boardDTO.getUserSeq(), boardSeq);
        
        List<BoardDTO> popularPosts = boardService.getPopularPosts();
        
        model.addAttribute("board", boardDTO);
        model.addAttribute("recentPosts", recentPosts);
        model.addAttribute("popularPosts", popularPosts);
        
        return "board/detail";
    }
    
    @GetMapping("/popularPostsAjax")
    @ResponseBody
    public ResponseEntity<List<BoardDTO>> popularPostsAjax() {
        List<BoardDTO> popularPosts = boardService.getPopularPosts();
        return ResponseEntity.ok(popularPosts);
    }

    @PostMapping("/upload")
    @ResponseBody
    public ResponseEntity<Map<String, String>> uploadFile(@RequestParam("file") MultipartFile file) {
        String savedFileName = FileService.saveFile(file, uploadPath);

        if (savedFileName == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "파일 업로드 실패"));
        }

        String fileUrl = "/uploads/" + savedFileName;
        Map<String, String> response = new HashMap<>();
        response.put("fileUrl", fileUrl);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/deleteFile")
    @ResponseBody
    public ResponseEntity<Map<String, String>> deleteFile(@RequestParam("fileUrl") String fileUrl) {
        String savedFileName = "";
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
