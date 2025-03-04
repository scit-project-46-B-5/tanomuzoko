package org.scit.project.board_heart.controller;

import org.scit.project.board_heart.dto.BoardHeartResponseDTO;
import org.scit.project.board_heart.service.BoardHeartService;
import org.scit.project.user.dto.LoginUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/heart")
@RequiredArgsConstructor
@Slf4j
public class BoardHeartController {

    private final BoardHeartService boardHeartService;

    @PostMapping("/toggle")
    public ResponseEntity<BoardHeartResponseDTO> toggleHeart(
            @RequestParam(name = "boardSeq") Long boardSeq,
            @AuthenticationPrincipal LoginUserDetails loginUser) {

        Long userId = loginUser.getUserSeq();
        BoardHeartResponseDTO response = boardHeartService.toggleHeart(boardSeq, userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/status")
    public ResponseEntity<BoardHeartResponseDTO> getHeartStatus(
            @RequestParam(name = "boardSeq") Long boardSeq,
            @AuthenticationPrincipal LoginUserDetails loginUser) {

        Long userId = (loginUser != null) ? loginUser.getUserSeq() : null;

        return ResponseEntity.ok(boardHeartService.getHeartStatus(boardSeq, userId));
    }
}
