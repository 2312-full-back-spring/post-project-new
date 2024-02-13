package com.github.postteam.web.controller;

import com.github.postteam.repository.user.UserEntity;
import com.github.postteam.service.post.PostService;
import com.github.postteam.web.dto.MsgResponseDto;
import com.github.postteam.web.dto.post.PostRequestDto;
import com.github.postteam.web.dto.post.PostResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostController {
    private final PostService postService;

    // 포스트 작성
    @PostMapping("/posts")
    public ResponseEntity<PostResponseDto> createPost(@RequestBody PostRequestDto postRequestDto, HttpServletRequest request){
        return ResponseEntity.ok(postService.createPost(postRequestDto, request));
    }

    // 포스트 전체 조회
    @GetMapping("/posts")
    public ResponseEntity<List<PostResponseDto>> getPostList(){
        return ResponseEntity.ok(postService.getPostList());
    }

    // 포스트 선택 조회
    @GetMapping("/posts/{postId}")
    public ResponseEntity<PostResponseDto> getPost(@PathVariable Integer postId){
        return ResponseEntity.ok(postService.getPost(postId));
    }

    // 포스트 이메일로 조회 (쿼리문)
    @GetMapping("/posts/search")
    public List<PostResponseDto> findPostByEmail(
            @RequestParam("author_email") String email){
        return postService.findPostsByEmail(email);
    }

    // 포스트 수정
    @PutMapping("/posts/{postId}")
    public ResponseEntity<PostResponseDto> updatePost(@PathVariable Integer postId,
                                                      @RequestBody PostRequestDto postRequestDto,
                                                      HttpServletRequest request){
        return ResponseEntity.ok(postService.updatePost(postId, postRequestDto, request));
    }

    // 포스트 삭제
    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<MsgResponseDto> deletePost(@PathVariable Integer postId, HttpServletRequest request){
        return ResponseEntity.ok(postService.deletePost(postId, request));
    }

    @PostMapping("/posts/like/{postId}")
    public ResponseEntity<Boolean> postLike(@RequestParam String like, @PathVariable Integer postId, HttpServletRequest request) {

        postService.postLike(like, postId, request);
        return ResponseEntity.status(HttpStatus.OK).body(true);
    }
}
