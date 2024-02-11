package com.github.postteam.web.dto.post;

import com.github.postteam.repository.post.PostEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostResponseDto {
    private Integer postId;
    private String title;
    private String content;
    private String author;
    private LocalDateTime createAt;

    public PostResponseDto(PostEntity postEntity){
        this.postId = postEntity.getPostId();
        this.title = postEntity.getTitle();
        this.content = postEntity.getContent();
        this.author = postEntity.getAuthor();
        this.createAt = postEntity.getCreateAt();
    }
}
