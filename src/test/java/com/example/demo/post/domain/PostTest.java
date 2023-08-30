package com.example.demo.post.domain;

import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class PostTest {
    @Test
    @DisplayName("게시글 생성")
    public void createPost(){
        // Given
        PostCreate postCreate = PostCreate.builder()
                .writerId(1)
                .content("hello")
                .build();
        User writer = User.builder()
                .email("geon1120@gmail.com")
                .nickname("geonhyeon")
                .address("gwangju")
                .status(UserStatus.ACTIVE)
                .certificationCode(UUID.randomUUID().toString())
                .build();

        // When
        Post post = Post.from(writer, postCreate);

        // Then
        assertThat(post.getContent()).isEqualTo(postCreate.getContent());
        assertThat(post.getWriter().getEmail()).isEqualTo(writer.getEmail());
        assertThat(post.getWriter().getNickname()).isEqualTo(writer.getNickname());
        assertThat(post.getWriter().getAddress()).isEqualTo(writer.getAddress());
        assertThat(post.getWriter().getStatus()).isEqualTo(writer.getStatus());
        assertThat(post.getWriter().getCertificationCode()).isEqualTo(writer.getCertificationCode());
    }

    @Test
    @DisplayName("게시글 수정")
    public void updatePost(){
        // Given
        // When
        // Then
    }
}
