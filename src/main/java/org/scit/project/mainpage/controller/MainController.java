package org.scit.project.mainpage.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.scit.project.mainpage.dto.MainDTO;
import org.scit.project.mainpage.dto.PostResponseDTO;
import org.scit.project.mainpage.service.MainService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MainController {

    private final MainService mainService;

    @GetMapping({ "/", "" })
    public String index() {

        return "index";
    }

    @GetMapping("/posts")
    @ResponseBody
    public PostResponseDTO getPosts(@RequestParam(value = "page", defaultValue = "0") int page) {
        List<MainDTO> list = mainService.getPosts(page);
        boolean isLastPage = mainService.isLastPage(page);

        return new PostResponseDTO(list , isLastPage);
    }
    
    @GetMapping("/top-posts")
    @ResponseBody
    public List<MainDTO> getTopPosts(@RequestParam(name = "filter", defaultValue = "weekly") String filter) {

        List<MainDTO> topPosts = mainService.getTopPosts(filter);
        return topPosts;
    }

    @GetMapping("/top-liked")
    @ResponseBody
    public List<MainDTO> getTopLikedPosts() {
        return mainService.getTopLikedPosts();
    }
}
