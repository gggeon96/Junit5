package com.example.demo.post.domain;

import com.example.demo.mock.MockClockHolder;
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
        Post post = Post.from(writer, postCreate, new MockClockHolder(123456789L));

        // Then
        assertThat(post.getContent()).isEqualTo(postCreate.getContent());
        assertThat(post.getCreatedAt()).isEqualTo(123456789L);
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
        PostUpdate postUpdate = PostUpdate.builder()
                .content("foo-bar")
                .build();
        User writer = User.builder()
                .id(1L)
                .email("geon1120@gmail.com")
                .nickname("geonhyeon")
                .address("gwangju")
                .status(UserStatus.ACTIVE)
                .certificationCode("abcdefgh-abcd-abcd-abcd-abcdefghijklm")
                .build();
        Post post = Post.builder()
                .id(1L)
                .writer(writer)
                .content("hello")
                .createdAt(123456789L)
                .modifiedAt(0L)
                .build();
        // When
        post = post.update(postUpdate, new MockClockHolder(123456789L));

        // Then
        assertThat(post.getContent()).isEqualTo(postUpdate.getContent());
        assertThat(post.getCreatedAt()).isEqualTo(123456789L);
        assertThat(post.getWriter().getEmail()).isEqualTo(writer.getEmail());
        assertThat(post.getWriter().getNickname()).isEqualTo(writer.getNickname());
        assertThat(post.getWriter().getAddress()).isEqualTo(writer.getAddress());
        assertThat(post.getWriter().getStatus()).isEqualTo(writer.getStatus());
        assertThat(post.getWriter().getCertificationCode()).isEqualTo(writer.getCertificationCode());
    }
}
