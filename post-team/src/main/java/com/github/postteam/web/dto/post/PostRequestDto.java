package com.github.postteam.web.dto.post;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostRequestDto {
    private String title;
    private String content;
}
