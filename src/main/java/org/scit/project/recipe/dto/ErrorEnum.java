package org.scit.project.recipe.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorEnum {
    SESSION_INVALIDATE("session이 만료되었습니다.");

    private final String message;
}
