package com.example.demo.repository;


import com.example.demo.model.UserStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
@DataJpaTest(showSql = true)
@TestPropertySource("classpath:test-application.properties")
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    void UserRepository_제대로_연결되었다(){
        //given
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail("geon1120@gmail.com");
        userEntity.setAddress("Seoul");
        userEntity.setNickname("gunn");
        userEntity.setStatus(UserStatus.ACTIVE);
        userEntity.setCertificationCode("aaaaaaaa-aaaaa-aaaa-aaaaa-aaaaaaaaaa");
        //when
        UserEntity result = userRepository.save(userEntity);
        //then

        assertThat(result.getId()).isNotNull();
    }

    @Test
    void findByIdANdStatus_가유저데이터를_찾는다(){
        //given
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail("geon1120@gmail.com");
        userEntity.setAddress("Seoul");
        userEntity.setNickname("gunn");
        userEntity.setStatus(UserStatus.ACTIVE);
        userEntity.setCertificationCode("aaaaaaaa-aaaaa-aaaa-aaaaa-aaaaaaaaaa");

        //when
        userRepository.save(userEntity);
        Optional<UserEntity> result = userRepository.findByIdAndStatus(1, UserStatus.ACTIVE);

        //then
        assertThat(result.isPresent()).isTrue();
    }

    @Test
    void findByIdAndStatus_에데이터가없으면_Optional_empty를내려준다(){
        //given
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail("geon1120@gmail.com");
        userEntity.setAddress("Seoul");
        userEntity.setNickname("gunn");
        userEntity.setStatus(UserStatus.ACTIVE);
        userEntity.setCertificationCode("aaaaaaaa-aaaaa-aaaa-aaaaa-aaaaaaaaaa");

        //when
        userRepository.save(userEntity);
        Optional<UserEntity> result = userRepository.findByIdAndStatus(1, UserStatus.PENDING);

        //then
        assertThat(result.isEmpty()).isTrue();
    }

    @Test
    void findsUserDataWithFindByEmailAndStatus(){
        //given
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail("geon1120@gmail.com");
        userEntity.setAddress("Seoul");
        userEntity.setNickname("gunn");
        userEntity.setStatus(UserStatus.ACTIVE);
        userEntity.setCertificationCode("aaaaaaaa-aaaaa-aaaa-aaaaa-aaaaaaaaaa");

        //when
        userRepository.save(userEntity);
        Optional<UserEntity> result = userRepository.findByEmailAndStatus("geon1120@gmail.com", UserStatus.ACTIVE);

        //then
        assertThat(result.isPresent()).isTrue();
    }

    @Test
    void returnsOptionalEmptyWhenThereIsNoUserData(){
        //given
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail("geon1120@gmail.com");
        userEntity.setAddress("Seoul");
        userEntity.setNickname("gunn");
        userEntity.setStatus(UserStatus.ACTIVE);
        userEntity.setCertificationCode("aaaaaaaa-aaaaa-aaaa-aaaaa-aaaaaaaaaa");

        //when
        userRepository.save(userEntity);
        Optional<UserEntity> result = userRepository.findByEmailAndStatus("geon1120@gmail.com", UserStatus.PENDING);

        //then
        assertThat(result.isEmpty()).isTrue();
    }
}
