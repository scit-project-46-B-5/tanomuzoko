package org.scit.project.mainpage.dto;

import org.scit.project.board.entity.BoardEntity;

public interface BoardWithHeartCountDTO {
    BoardEntity getBoard();
    int getHeartCount();
}
