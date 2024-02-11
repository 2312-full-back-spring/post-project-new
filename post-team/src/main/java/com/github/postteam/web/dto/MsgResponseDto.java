package com.github.postteam.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MsgResponseDto {
    private String msg;
    private int statusCode;
}
