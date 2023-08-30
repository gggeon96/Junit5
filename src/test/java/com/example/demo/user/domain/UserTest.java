package com.example.demo.user.domain;

import com.example.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.example.demo.mock.MockClockHolder;
import com.example.demo.mock.MockUuidHolder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.util.Assert.isInstanceOf;

public class UserTest {
    @Test
    @DisplayName("유저 도메인 생성")
    public void createUserDomainWithUserCreate(){
        // Given
        UserCreate userCreate = UserCreate.builder()
                .email("geon1120@gmail.com")
                .nickname("geonhyeon")
                .address("gwangju")
                .build();

        // When
        User user = User.from(userCreate,new MockUuidHolder("abcdefgh-abcd-abcd-abcd-abcdefghijklm"));

        // Then
        assertThat(user.getNickname()).isEqualTo(userCreate.getNickname());
        assertThat(user.getEmail()).isEqualTo(userCreate.getEmail());
        assertThat(user.getAddress()).isEqualTo(userCreate.getAddress());
        assertThat(user.getStatus()).isEqualTo(UserStatus.PENDING);
        assertThat(user.getCertificationCode()).isEqualTo("abcdefgh-abcd-abcd-abcd-abcdefghijklm");
    }

    @Test
    @DisplayName("유저 도메인 수정")
    public void updateUserDomainWithUserUpdate(){
        // Given
        User user = User.builder()
                .id(1L)
                .email("geon1120@gmail.com")
                .nickname("geonhyeon")
                .address("gwangju")
                .status(UserStatus.ACTIVE)
                .certificationCode(new MockUuidHolder("abcdefgh-abcd-abcd-abcd-abcdefghijklm").random())
                .lastLoginAt(100L)
                .build();
        UserUpdate userUpdate = UserUpdate.builder()
            .nickname("geonhyeonkim")
            .address("incheon")
            .build();

        // When
        user = user.update(userUpdate);

        // Then
        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getEmail()).isEqualTo("geon1120@gmail.com");
        assertThat(user.getNickname()).isEqualTo(userUpdate.getNickname());
        assertThat(user.getAddress()).isEqualTo(userUpdate.getAddress());
        assertThat(user.getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(user.getCertificationCode()).isEqualTo("abcdefgh-abcd-abcd-abcd-abcdefghijklm");
        assertThat(user.getLastLoginAt()).isEqualTo(100L);
    }

    @Test
    @DisplayName("로그인시 마지막 로그인 시간이 바뀐다")
    public void updateLastLoginAtWhenLogin(){
        // Given
        User user = User.builder()
                .id(1L)
                .email("geon1120@gmail.com")
                .nickname("geonhyeon")
                .address("gwangju")
                .status(UserStatus.ACTIVE)
                .certificationCode(new MockUuidHolder("abcdefgh-abcd-abcd-abcd-abcdefghijklm").random())
                .lastLoginAt(100L)
                .build();
        // When
        user = user.login(new MockClockHolder(12345678L));

        // Then
        assertThat(user.getLastLoginAt()).isEqualTo(12345678L);
    }

    @Test
    @DisplayName("User는 인증코드로 계정을 활성화 상태로 변경한다")
    public void updateStatusToActiveWhenCertificateSuccess(){
        // Given
        User user = User.builder()
                .id(1L)
                .email("geon1120@gmail.com")
                .nickname("geonhyeon")
                .address("gwangju")
                .status(UserStatus.PENDING)
                .certificationCode(new MockUuidHolder("abcdefgh-abcd-abcd-abcd-abcdefghijklm").random())
                .lastLoginAt(100L)
                .build();

        // When
        user = user.certificate("abcdefgh-abcd-abcd-abcd-abcdefghijklm");

        // Then
        assertThat(user.getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    @DisplayName("User는 잘못된 인증코드를 입력할 시 예외를 던진다")
    public void throwExceptionWhenCertificateCalledWithInvalidCode(){
        // Given
        User user = User.builder()
                .id(1L)
                .email("geon1120@gmail.com")
                .nickname("geonhyeon")
                .address("gwangju")
                .status(UserStatus.PENDING)
                .certificationCode(new MockUuidHolder("abcdefgh-abcd-abcd-abcd-abcdefghijklm").random())
                .lastLoginAt(100L)
                .build();

        // When

        // Then
        assertThatThrownBy(() -> user.certificate("abcdefgh-abcd-abcd-abcd-abcdefghijkln"))
                .isInstanceOf(CertificationCodeNotMatchedException.class);
    }
}
