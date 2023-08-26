package com.example.demo.user.service;

import com.example.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.UserCreate;
import com.example.demo.user.domain.UserUpdate;
import com.example.demo.user.infrastructure.UserEntity;
import com.example.demo.user.service.UserService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@TestPropertySource("classpath:test-application.properties")
@SqlGroup({
    @Sql(value = "/sql/user-service-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
    @Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
//@DataJpaTest
public class UserServiceTest {
    @Autowired
    private UserService userService;
    @MockBean
    private JavaMailSender javaMailSender;

    @Test
    void getByEmail_가_ACTIVE_상태인_유저를_가져온다(){
        //given
        String email = "geon1120@gmail.com";

        //when
        UserEntity user = userService.getByEmail(email);

        //then
        assertThat(user.getNickname()).isEqualTo("geonhyeon");
    }

    @Test
    void getByEmail_가_PENDING_상태인_유저는_찾아오지_못한다(){
        //given
        String email = "geon1122@gmail.com";

        //when

        //then
        assertThatThrownBy(() -> {userService.getByEmail(email);}).isInstanceOf(ResourceNotFoundException.class);

    }

    @Test
    void getById_가_ACTIVE_상태인_유저를_가져온다(){
        //given

        //when
        UserEntity user = userService.getById(1);

        //then
        assertThat(user.getNickname()).isEqualTo("geonhyeon");
    }

    @Test
    void getById_가_PENDING_상태인_유저는_찾아오지_못한다(){
        //given

        //when

        //then
        assertThatThrownBy(() -> {userService.getById(2);}).isInstanceOf(ResourceNotFoundException.class);

    }



    @Test
//    @Disabled
    void UserCreateDto를_이용하여_유저를_생성할_수_있다(){
        //given
        UserCreate userCreate = UserCreate.builder()
                .email("geon1120@gmail.com")
                .address("seoul")
                .nickname("gunn")
                .build();
        BDDMockito.doNothing().when(javaMailSender).send(BDDMockito.any(SimpleMailMessage.class));

        //when
        UserEntity user = userService.create(userCreate);

        //then
        assertThat(user.getId()).isNotNull();
        assertThat(user.getStatus()).isEqualTo(UserStatus.PENDING);
        //아직 확인 못하는 UUID
//        assertThat(user.getCertificationCode()).isEqualTo("not yet.. TT");
    }

    @Test
    void UserUpdateDto를_이용하여_유저를_수정할_수_있다(){
        //given
        UserUpdate userUpdateDto = UserUpdate.builder()
                .address("incheon")
                .nickname("gunnhyeon")
                .build();

        //when
        userService.update(1,userUpdateDto);

        //then
        UserEntity user = userService.getById(1);
        assertThat(user.getId()).isNotNull();
        assertThat(user.getAddress()).isEqualTo("incheon");
        assertThat(user.getNickname()).isEqualTo("gunnhyeon");
    }

    @Test
    void login_할_수_있다(){
        //given

        //when
        userService.login(1);

        //then
        UserEntity user = userService.getById(1);
        assertThat(user.getLastLoginAt()).isGreaterThan(0L);
        //millis를 이용한 체크는 아직 안된다

    }

    @Test
    void PENDING_상태사용자는_인증코드로_활성화(){
        //given
        userService.verifyEmail(2,"abcdefgh-abcd-abcd-abcd-abcdefghijklz");

        //when

        //then
        UserEntity user = userService.getById(2);
        assertThat(user.getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    void PENDING_상태사용자는_잘못된_인증코드는_활성화되지_않는다(){
        //given

        //when

        //then
        assertThatThrownBy(()-> {userService.verifyEmail(2,"notvalidcode");}).isInstanceOf(CertificationCodeNotMatchedException.class);
    }
}
