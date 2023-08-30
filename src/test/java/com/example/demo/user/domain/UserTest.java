package com.example.demo.user.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UserTest {
    @Test
    @DisplayName("유저 도메인 생성")
    public void createUserDomainWithUserCreate(){
        // Given
        // When
        // Then
    }

    @Test
    @DisplayName("유저 도메인 수정")
    public void updateUserDomainWithUserUpdate(){
        // Given
        // When
        // Then
    }

    @Test
    @DisplayName("로그인시 마지막 로그인 시간이 바뀐다")
    public void updateLastLoginAtWhenLogin(){
        // Given
        // When
        // Then
    }

    @Test
    @DisplayName("User는 인증코드로 계정을 활성화 상태로 변경한다")
    public void updateStatusToActiveWhenCertificateSuccess(){
        // Given
        // When
        // Then
    }

    @Test
    @DisplayName("User는 잘못된 인증코드를 입력할 시 예외를 던진다")
    public void throwExceptionWhenCertificateCalledWithInvalidCode(){
        // Given
        // When
        // Then
    }
}
