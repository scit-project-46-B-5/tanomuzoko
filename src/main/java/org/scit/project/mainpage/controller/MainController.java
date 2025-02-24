package org.scit.project.mainpage.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.scit.project.mainpage.dto.MainDTO;
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
    public Map<String, Object> getPosts(@RequestParam(value = "page", defaultValue = "0") int page) {
        Map<String, Object> response = new HashMap<>();

        List<MainDTO> list = mainService.getPosts(page);

        boolean isLastPage = mainService.isLastPage(page);

        response.put("posts", list);
        response.put("isLastPage", isLastPage);

        return response;
    }
    
    @GetMapping("/top-posts")
    @ResponseBody
    public List<MainDTO> getTopPosts(@RequestParam(name = "filter", defaultValue = "weekly") String filter) {
        
        List<MainDTO> topPosts = mainService.getTopPosts(filter);
        return topPosts;
    }

}
