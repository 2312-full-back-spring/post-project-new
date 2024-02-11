package com.github.postteam.service.auth;

import com.github.postteam.config.security.JwtUtil;
import com.github.postteam.repository.user.UserEntity;
import com.github.postteam.repository.user.UserRepository;
import com.github.postteam.web.dto.auth.LoginRequestDto;
import com.github.postteam.web.dto.auth.SignupRequestDto;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;

@Service
@Validated
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // 회원가입
    public void signup(SignupRequestDto signupRequestDto) {
        String email = signupRequestDto.getEmail();
        String password = signupRequestDto.getPassword();

        // 회원 중복 확인
        Optional<UserEntity> checkEmail = userRepository.findByEmail(email);
        if(checkEmail.isPresent()) {
            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
        }

        // 조회한 이메일이 DB에 없을 경우, 사용자 등록
        UserEntity userEntity = new UserEntity(email, password);
        userRepository.save(userEntity);
    }

    // 로그인
    public void login(LoginRequestDto loginRequestDto, HttpServletResponse response) {
        String email = loginRequestDto.getEmail();
        String password = loginRequestDto.getPassword();

        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(
                () -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다.")
        );

        // 비밀번호 일치 여부 확인
        if(!userEntity.getPassword().equals(password)){
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, JwtUtil.createToken(userEntity.getEmail()));
    }
}
