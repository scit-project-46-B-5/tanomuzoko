package org.scit.project.mainpage.service;

import java.util.ArrayList;
import java.util.List;

import org.scit.project.board.entity.BoardEntity;
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

    public List<MainDTO> getPosts(int page) {

        int pageSize = 10;

        Page<BoardEntity> temp = mainRepository
                .findAll(PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "createDate")));

        List<MainDTO> list = new ArrayList<>();

        temp.forEach((entity) -> list.add(MainDTO.toDTO(entity)));

        return list;
    }

}
