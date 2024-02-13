package com.github.postteam.repository.comment;

import com.github.postteam.repository.post.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;


public interface CommentRepository extends JpaRepository<Comment, Integer> {

    //특정 게시글의 모든 댓글 조회
    @Query(value = "SELECT *FROM comment WHERE post_id= :postId",
            nativeQuery = true)
    List <Comment> findAllByPostId(Integer postId);

    List<Comment> findByPostPostId(Integer postId);

    //특정 닉네임의 모든 댓글 조회
    List<Comment> findAllByNickname(String nickname);
}
