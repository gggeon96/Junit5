package com.example.demo.user.controller.response;

import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class UserResponseTest {
    @Test
    @DisplayName("userResponse 생성")
    public void createUserResponseWithUser(){
        // Given
        User user = User.builder()
                .id(1L)
                .email("geon1120@gmail.com")
                .nickname("geonhyeon")
                .address("gwangju")
                .status(UserStatus.ACTIVE)
                .lastLoginAt(100L)
                .certificationCode(UUID.randomUUID().toString())
                .build();

        // When
        UserResponse userResponse = UserResponse.from(user);

        // Then
        assertThat(userResponse.getId()).isEqualTo(1);
        assertThat(userResponse.getEmail()).isEqualTo(user.getEmail());
        assertThat(userResponse.getNickname()).isEqualTo(user.getNickname());
        assertThat(userResponse.getStatus()).isEqualTo(user.getStatus());
        assertThat(userResponse.getLastLoginAt()).isEqualTo(user.getLastLoginAt());
    }
}
