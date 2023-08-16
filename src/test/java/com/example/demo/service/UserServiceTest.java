package com.example.demo.service;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
public class UserServiceTest {
    @Autowired
    private UserService userService;

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

}
