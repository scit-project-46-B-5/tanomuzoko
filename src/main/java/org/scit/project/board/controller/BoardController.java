package org.scit.project.board.controller;

import org.scit.project.board.dto.BoardDTO;
import org.scit.project.board.service.BoardService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

		return "/board/detail"; // Thymeleaf에서 사용할 데이터 전달
	}

}
