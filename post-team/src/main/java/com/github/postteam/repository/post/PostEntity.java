package com.github.postteam.repository.post;

import com.github.postteam.repository.user.UserEntity;
import com.github.postteam.web.dto.post.PostRequestDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "posts")
public class PostEntity {
    @Id
    @Column(name = "post_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer postId;

    // post : user = N : 1 다대일 단방향 연관관계
    @ManyToOne(fetch = FetchType.LAZY)      //
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity userId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "author")
    private String author;

    @Column(name = "create_at")
    private LocalDateTime createAt;

    // 게시글 작성
    public PostEntity(PostRequestDto requestDto, UserEntity userEntity) {
        this.userId = userEntity;
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.author = userEntity.getEmail();
        this.createAt = LocalDateTime.now();
    }

    // 게시글 수정
    public void update(PostRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
    }
}
