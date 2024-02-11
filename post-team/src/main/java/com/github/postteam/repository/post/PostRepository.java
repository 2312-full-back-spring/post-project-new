package com.github.postteam.repository.post;

import com.github.postteam.repository.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, Integer> {
    Optional<PostEntity> findByPostIdAndUserId(Integer PostId, UserEntity userId);
}
