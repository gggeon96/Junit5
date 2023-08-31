package com.example.demo.post.service;

import com.example.demo.mock.*;
import com.example.demo.post.domain.Post;
import com.example.demo.post.domain.PostCreate;
import com.example.demo.post.domain.PostUpdate;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.service.CertificationService;
import com.example.demo.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class PostServiceTest {

    private PostService postService;
    @BeforeEach
    void init(){
        MockPostRepository mockPostRepository = new MockPostRepository();
        MockUserRepository mockUserRepository = new MockUserRepository();

        this.postService = PostService.builder()
                .postRepository(mockPostRepository)
                .userRepository(mockUserRepository)
                .clockHolder(new MockClockHolder(123456789))
                .build();
        mockUserRepository.save(User.builder()
                .id(1L)
                .email("geon1120@gmail.com")
                .nickname("geonhyeon")
                .address("gwangju")
                .certificationCode("abcdefgh-abcd-abcd-abcd-abcdefghijklm")
                .status(UserStatus.ACTIVE)
                .lastLoginAt(123456789L)
                .build());
        mockUserRepository.save(User.builder()
                .id(2L)
                .email("geon1122@gmail.com")
                .nickname("geonhyeon1")
                .address("gwangju")
                .certificationCode("abcdefgh-abcd-abcd-abcd-abcdefghijklz")
                .status(UserStatus.PENDING)
                .lastLoginAt(123456789L)
                .build());
        mockPostRepository.save(Post.builder()
                .id(1L)
                .content("helloworld")
                .createdAt(123456789L)
                .modifiedAt(123456789L)
                .writer(mockUserRepository.getById(1L))
                .build());
    }

    @Test
    void getById는_존재하는_게시물을_내려준다() {
        // given
        // when
        Post result = postService.getById(1L);

        // then
        assertThat(result.getContent()).isEqualTo("helloworld");
        assertThat(result.getWriter().getEmail()).isEqualTo("geon1120@gmail.com");
    }

    @Test
    void postCreateDto_를_이용하여_게시물을_생성할_수_있다() {
        // given
        PostCreate postCreateDto = PostCreate.builder()
                .writerId(1)
                .content("foobar")
                .build();

        // when
        Post result = postService.create(postCreateDto);

        // then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getContent()).isEqualTo("foobar");
    }

    @Test
    void postUpdateDto_를_이용하여_게시물을_수정할_수_있다() {
        // given
        PostUpdate postUpdate = PostUpdate.builder()
                .content("hello world :)")
                .build();

        // when
        postService.update(1, postUpdate);

        // then
        Post post = postService.getById(1);
        assertThat(post.getContent()).isEqualTo("hello world :)");
        assertThat(post.getModifiedAt()).isEqualTo(123456789L);
    }

}
