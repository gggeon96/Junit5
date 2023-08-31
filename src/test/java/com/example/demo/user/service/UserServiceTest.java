package com.example.demo.user.service;

import com.example.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.mock.FakeMailSender;
import com.example.demo.mock.MockClockHolder;
import com.example.demo.mock.MockUserRepository;
import com.example.demo.mock.MockUuidHolder;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserCreate;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.UserUpdate;
import org.junit.jupiter.api.BeforeEach;
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

public class UserServiceTest {

    private UserService userService;
    @BeforeEach
    void init(){
        FakeMailSender fakeMailSender = new FakeMailSender();
        MockUserRepository mockUserRepository = new MockUserRepository();
        this.userService = UserService.builder()
                .certificationService(new CertificationService(fakeMailSender))
                .clockHolder(new MockClockHolder(123456789))
                .uuidHolder(new MockUuidHolder("abcdefgh-abcd-abcd-abcd-abcdefghijklm"))
                .userRepository(mockUserRepository)
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
    }

    @Test
    void getByEmail_가_ACTIVE_상태인_유저를_가져온다(){
        //given
        String email = "geon1120@gmail.com";

        //when
        User user = userService.getByEmail(email);

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
        User user = userService.getById(1);

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

        //when
        User user = userService.create(userCreate);

        //then
        assertThat(user.getId()).isNotNull();
        assertThat(user.getStatus()).isEqualTo(UserStatus.PENDING);
        assertThat(user.getCertificationCode()).isEqualTo("abcdefgh-abcd-abcd-abcd-abcdefghijklm");
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
        User user = userService.getById(1);
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
        User user = userService.getById(1);
        assertThat(user.getLastLoginAt()).isEqualTo(123456789);

    }

    @Test
    void PENDING_상태사용자는_인증코드로_활성화(){
        //given
        userService.verifyEmail(2,"abcdefgh-abcd-abcd-abcd-abcdefghijklz");

        //when

        //then
        User user = userService.getById(2);
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
