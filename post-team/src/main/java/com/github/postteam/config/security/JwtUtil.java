package com.github.postteam.config.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Slf4j(topic = "JwtUtil")
@Component
public class JwtUtil {
    //Authorization: Bearer <JWT> 에서 Header 에 들어가는 KEY 값
    public static final String AUTHORIZATION_HEADER = "Authorization";

    // 사용자 권한 값의 KEY: 사용자 권한도 Token 안에 넣는데, 이를 가져올 때 사용하는 KEY 값
    public static final String AUTHORIZATION_KEY = "auth";

    // Token 식별자: Token 을 만들 때, 앞에 들어가는 부분
    public static final String BEARER_PREFIX = "Bearer ";

    // 토큰 만료시간: 밀리세컨드 기준. 60 * 1000L는 1분. 60 * 60 * 1000L는 1시간
    private static final long TOKEN_TIME = 60 * 60 * 1000L;

    // Base64 Encode 한 SecretKey (application.yml 에 추가해둔 값)
    @Value("${jwt.secret-key-source}")
    private String secretKey;

    //Key 객체: Token 을 만들 때 넣어줄 KEY 값
    private static Key key;

    // 사용할 알고리즘 선택
    private static final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;


    //종속성 주입이 완료된 후 실행되어야 하는 메소드에 사용
    //처음에 객체가 생성될 때, 초기화하는 함수
    //사용 이유?
    //1. 생성자가 호출되었을 때, 빈은 초기화되지 않았음(의존성 주입이 이루어지지 않았음)
    //2. 어플리케이션이 실행될 때 bean 이 오직 한 번만 수행되게 해서  bean 이 여러 번 초기화되는 걸 방지
    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    // JWT 토큰 생성
    public static String createToken(String email) {
        Date date = new Date();

        // 암호화
        return BEARER_PREFIX +    // Token 앞에 들어가는 부분
                Jwts.builder()
                        .setSubject(email)               // 사용자 식별자값(ID). 여기에선 email 을 넣음
                        .claim(AUTHORIZATION_KEY, email)     // 사용자 권한 (key, value)
                        .setExpiration(new Date(date.getTime() + TOKEN_TIME))   // 만료 시간 : 현재시간 date.getTime() + 위에서 지정한 토큰 만료시간(60분)
                        .setIssuedAt(date)                  // 발급일
                        .signWith(key, signatureAlgorithm)  // 암호화 알고리즘 (Secret key, 사용할 알고리즘 종류)
                        .compact();
    }

    // header에서 JWT 가져오기
    public String getJwtFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }

        return null;
    }

    // JWT 토큰 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token, 만료된 JWT token 입니다.");
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            log.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        return false;
    }

    // JWT 토큰에서 사용자 정보 가져오기
    public Claims getUserInfoFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();     // body 에 있는 claims 를 가져온다
    }

}
