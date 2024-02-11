package com.github.postteam.repository.like;

import com.github.postteam.repository.post.PostEntity;
import com.github.postteam.repository.user.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "like")
public class LikeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_id")
    private Integer LikeId;

    @JoinColumn(name = "user_id")
    @ManyToOne
    private UserEntity userEntity;

    @JoinColumn(name = "post_id")
    @ManyToOne
    private PostEntity postEntity;

    public LikeEntity(UserEntity userEntity, PostEntity postEntity) {
        this.userEntity = userEntity;
        this.postEntity = postEntity;
    }
}
