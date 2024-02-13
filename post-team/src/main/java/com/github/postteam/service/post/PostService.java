package com.github.postteam.service.post;

import com.github.postteam.config.security.JwtUtil;
import com.github.postteam.repository.like.LikeEntity;
import com.github.postteam.repository.like.LikeRepository;
import com.github.postteam.repository.post.PostEntity;
import com.github.postteam.repository.post.PostRepository;
import com.github.postteam.repository.user.UserEntity;
import com.github.postteam.repository.user.UserRepository;
import com.github.postteam.service.exception.NotFoundException;
import com.github.postteam.web.dto.MsgResponseDto;
import com.github.postteam.web.dto.post.PostRequestDto;
import com.github.postteam.web.dto.post.PostResponseDto;
import io.jsonwebtoken.Claims;
import jakarta.persistence.NoResultException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;
    private final JwtUtil jwtUtil;

    // 포스트 작성
    public PostResponseDto createPost(PostRequestDto postRequestDto, HttpServletRequest request) {
        UserEntity userEntity = getUserFromToken(request);

        PostEntity postEntity = new PostEntity(postRequestDto, userEntity);
        PostEntity savePost = postRepository.save(postEntity);

        return new PostResponseDto(savePost);
    }

    // 포스트 전체 조회
    public List<PostResponseDto> getPostList() {
        List<PostResponseDto> postList = postRepository.findAll().stream()
                                                                    .map(PostResponseDto::new)
                                                                    .toList();
        if (postList.isEmpty()){
            throw new NotFoundException("포스트를 찾을 수 없습니다.");
        }
        return postList;
    }

    // 포스트 선택 조회
    public PostResponseDto getPost(Integer postId) {

        // 해당 postId 가 없는 경우
        PostEntity postEntity = postRepository.findById(postId).orElseThrow(
                () -> new NotFoundException("포스트가 존재하지 않습니다.")
        );

        // 해당 postId 가 있는 경우
        return new PostResponseDto(postEntity);
    }

    // 포스트 이메일로 조회
    public List<PostResponseDto> findPostsByEmail(String email) {
        List<PostEntity> postEntities = postRepository.findPostEntitiesByAuthor(email);
        return postEntities.stream().map(PostResponseDto::new).toList();
    }

    // 포스트 수정
    @Transactional
    public PostResponseDto updatePost(Integer postId, PostRequestDto postRequestDto, HttpServletRequest request) {
        UserEntity userEntity = getUserFromToken(request);

        PostEntity postEntity = postRepository.findByPostIdAndUserId(postId, userEntity).orElseThrow(
                () -> new NotFoundException("해당 사용자의 포스트를 찾을 수 없습니다.")
        );

        postEntity.update(postRequestDto);

        return new PostResponseDto(postEntity);
    }

    // 포스트 삭제
    public MsgResponseDto deletePost(Integer postId, HttpServletRequest request) {
        UserEntity userEntity = getUserFromToken(request);

        PostEntity postEntity = postRepository.findByPostIdAndUserId(postId, userEntity).orElseThrow(
                () -> new NoResultException("해당 사용자의 포스트를 찾을 수 없습니다.")
        );

        postRepository.delete(postEntity);

        return new MsgResponseDto("포스트가 성공적으로 삭제되었습니다.", HttpStatus.OK.value());
    }

    // 토큰으로부터 사용자 정보 가져오는 메소드
    private UserEntity getUserFromToken(HttpServletRequest request) {
        String token = jwtUtil.getJwtFromHeader(request);       // request 에서 Token 가져오기
        Claims claims;          // JWT 안에 있는 정보를 담는 Claims 객체

        if (StringUtils.hasText(token)) {        // JWT 토큰 있는지 확인
            if (jwtUtil.validateToken(token)) {     // JWT 토큰 검증
                claims = jwtUtil.getUserInfoFromToken(token);       // ture 일 경우, JWT 토큰에서 사용자 정보 가져오기
            } else {
                throw new IllegalArgumentException("올바른 token 이 아닙니다.");
            }

            // 검증된 JWT 토큰에서 사용자 정보 조회 및 가져오기
            return userRepository.findByEmail(claims.getSubject()).orElseThrow(
                    () -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다.")
            );
        }

        throw new IllegalArgumentException("token 이 없습니다.");
    }


    // 좋아요 생성 및 삭제
    public void postLike(
          String like,
          Integer postId,
          HttpServletRequest request
    ) {
        UserEntity userEntity = getUserFromToken(request);
        PostEntity postEntity = postRepository.findById(postId).orElseThrow(
                () -> new NoResultException("해당 포스트를 찾을 수 없습니다.")
        );
        LikeEntity likeEntity = new LikeEntity(userEntity, postEntity);

        if (Objects.equals(like, "true")) {
            likeRepository.save(likeEntity);
        } else  {
            likeRepository.delete(likeEntity);
        }
    }
}
