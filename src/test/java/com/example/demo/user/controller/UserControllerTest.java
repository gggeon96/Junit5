package com.example.demo.user.controller;
import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.UserUpdate;
import com.example.demo.user.infrastructure.UserEntity;
import com.example.demo.user.infrastructure.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@SqlGroup({
        @Sql(value = "/sql/user-controller-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("사용자는 특정 유저의 정보를 전달 받을 수 있다")
    void getUserById() throws Exception {
        //given
        //when
        //then
        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nickname").value("geonhyeon"))
                .andExpect(jsonPath("$.email").value("geon1120@gmail.com"))
                .andExpect(jsonPath("$.address").doesNotExist())
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    @DisplayName("존재하지 않는 유저의 Id를 전달하면 404를 반환한다")
    void getUserByIdWithNonExistId() throws Exception {
        //given
        //when
        //then
        mockMvc.perform(get("/api/users/1231233"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Users에서 ID 1231233를 찾을 수 없습니다."));

    }

    @Test
    @DisplayName("인증코드로 계정을 활성화 할 수 있다.")
    void verifyEmail() throws Exception {
        mockMvc.perform(get("/api/users/2/verify")
                .queryParam("certificationCode", "abcdefgh-abcd-abcd-abcd-abcdefghijklz"))
                .andExpect(status().isFound());
        UserEntity userEntity = userRepository.findById(2L).get();
        assertThat(userEntity.getStatus()).isEqualTo(UserStatus.ACTIVE);

    }

    @Test
    @DisplayName("내 정보를 불러올 때 주소도 갖고 올 수 있다")
    void getMyInfo() throws Exception {
        mockMvc.perform(
                get("/api/users/me")
                        .header("EMAIL","geon1120@gmail.com")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.address").value("seoul"))
                .andExpect(jsonPath("$.nickname").value("geonhyeon"))
                .andExpect(jsonPath("$.email").value("geon1120@gmail.com"))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    @DisplayName("사용자는 내 정보를 수정 할 수 있다.")
    void updateMyInfo() throws Exception {
        //given
        UserUpdate userUpdateDto = UserUpdate.builder()
                    .nickname("geonhyeon-modified")
                    .address("Gwangju")
                    .build();

        //when
        mockMvc.perform(put("/api/users/me")
                .header("EMAIL","geon1120@gmail.com")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(userUpdateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.address").value("Gwangju"))
                .andExpect(jsonPath("$.nickname").value("geonhyeon-modified"))
                .andExpect(jsonPath("$.email").value("geon1120@gmail.com"))
                .andExpect(jsonPath("$.status").value("ACTIVE"));

    }
}