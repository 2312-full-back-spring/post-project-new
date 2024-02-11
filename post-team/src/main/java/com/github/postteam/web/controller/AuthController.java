package com.github.postteam.web.controller;

import com.github.postteam.service.auth.UserService;
import com.github.postteam.web.dto.MsgResponseDto;
import com.github.postteam.web.dto.auth.LoginRequestDto;
import com.github.postteam.web.dto.auth.SignupRequestDto;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<MsgResponseDto> signup(@RequestBody @Valid SignupRequestDto signupRequestDto){
        userService.signup(signupRequestDto);
        return ResponseEntity.ok(new MsgResponseDto("회원가입이 완료되었습니다.", HttpStatus.OK.value()));
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<MsgResponseDto> login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
        userService.login(loginRequestDto, response);
        return ResponseEntity.ok(new MsgResponseDto("로그인이 성공적으로 완료되었습니다.", HttpStatus.OK.value()));
    }
}
