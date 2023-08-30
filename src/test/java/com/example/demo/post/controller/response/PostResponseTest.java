package com.example.demo.post.controller.response;

import com.example.demo.post.domain.Post;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class PostResponseTest {
    @Test
    @DisplayName("postResponse 생성")
    public void createPostResponseWithPost(){
        // Given
        Post post = Post.builder()
                .id(1L)
                .writer(User.builder()
                        .email("geon1120@gmail.com")
                        .nickname("geonhyeon")
                        .address("gwangju")
                        .status(UserStatus.ACTIVE)
                        .certificationCode(UUID.randomUUID().toString())
                        .build()
                )
                .content("hello")
                .build();
        // When
        PostResponse postResponse = PostResponse.from(post);

        // Then
        assertThat(post.getId()).isEqualTo(postResponse.getId());
        assertThat(post.getContent()).isEqualTo(postResponse.getContent());
        assertThat(post.getWriter().getNickname()).isEqualTo("geonhyeon");
    }
}
