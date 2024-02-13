package com.github.postteam.web.dto.comment;

import com.github.postteam.repository.comment.Comment;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class CommentDto {
    private Integer id; //댓글 id
    private Integer postId; // 댓글의 부모 id
    private String nickname; //댓글 작성자
    @Setter
    private String body; //댓글 내용

    public static CommentDto createCommentDto(Comment comment) {
        return new CommentDto(
                comment.getId(), //댓글 엔티티 id
                comment.getPost().getPostId(), //댓글 엔티티 속한 부모 게시글 id
                comment.getNickname(),
                comment.getBody()
        );
    }

    public CommentDto(Integer postId, String nickname, String body) {
        this.postId = postId;
        this.nickname = nickname;
        this.body = body;
    }
}