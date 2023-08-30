package com.example.demo.user.service;

import com.example.demo.mock.FakeMailSender;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


class CertificationServiceTest {

    @Test
    @DisplayName("이메일과 컨텐츠가 제대로 만들어졌는가")
    void send() {
        //given
        FakeMailSender fakeMailSender = new FakeMailSender();
        CertificationService certificationService = new CertificationService(fakeMailSender);
        //when
        certificationService.send("geon1120@gmail.com",1,"abcdefgh-abcd-abcd-abcd-abcdefghijklz");
        //then
        assertThat(fakeMailSender.email).isEqualTo("geon1120@gmail.com");
        assertThat(fakeMailSender.title).isEqualTo("Please certify your email address");
        assertThat(fakeMailSender.content).isEqualTo("Please click the following link to certify your email address: http://localhost:8080/api/users/1/verify?certificationCode=abcdefgh-abcd-abcd-abcd-abcdefghijklz");
    }

}