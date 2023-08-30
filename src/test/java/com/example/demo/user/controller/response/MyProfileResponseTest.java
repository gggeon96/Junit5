package com.example.demo.user.controller.response;

import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class MyProfileResponseTest {
    @Test
    @DisplayName("MyProfileResponse 생성")
    public void createMyProfileResponse(){
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
        MyProfileResponse myProfileResponse = MyProfileResponse.from(user);

        // Then
        assertThat(myProfileResponse.getId()).isEqualTo(1);
        assertThat(myProfileResponse.getEmail()).isEqualTo(user.getEmail());
        assertThat(myProfileResponse.getAddress()).isEqualTo(user.getAddress());
        assertThat(myProfileResponse.getStatus()).isEqualTo(user.getStatus());
        assertThat(myProfileResponse.getLastLoginAt()).isEqualTo(user.getLastLoginAt());
    }
}
