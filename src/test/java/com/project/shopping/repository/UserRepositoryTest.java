package com.project.shopping.repository;

import com.project.shopping.config.TestConfig;
import com.project.shopping.dto.requestDTO.UserRequestDTO.UserJoinRequestDTO;
import com.project.shopping.model.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.internal.junit.JUnitTestRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;

import static org.junit.jupiter.api.Assertions.*;


//실제 DB를 사용하여 테스트 진행, CRUD 테스트를 통해 데이터베이스 연결 확인
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Import(TestConfig.class)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("## 유저 저장하기 레파지토리 테스트 ##")
    public void saveUser () throws Exception{

        //given
        User user = User.builder()
                .email("test@gmail.com")
                .password("test")
                .username("test")
                .address("test")
                .postCode("test")
                .age(20)
                .nickname("test")
                .phoneNumber("010-0000-0000")
                .status("active")
                .build();

        //when
        User savedUser = userRepository.save(user);

        //then
        assertThat(user).isSameAs(savedUser);
    }

    @Test
    @DisplayName("## 유저 조회하기 레파지토리 테스트 ##")
    public void getUser () throws Exception{


        String email = "test@gmail.com";

        //given
        User user = User.builder()
                .email("test@gmail.com")
                .password("test")
                .username("test")
                .address("test")
                .postCode("test")
                .age(20)
                .nickname("test")
                .phoneNumber("010-0000-0000")
                .status("active")
                .build();

        User savedUser = userRepository.save(user);


        //when
        User findUser = userRepository.findByEmail(user.getEmail()).get();


        //then
        assertThat(savedUser).isSameAs(findUser);
    }

    @Test
    @DisplayName("## 유저 정보 갱신하기 레파지토리 테스트 ##")
    public void updateUser () throws Exception{


        //given
        User user = User.builder()
                .email("test@gmail.com")
                .password("test")
                .username("test")
                .address("test")
                .postCode("test")
                .age(20)
                .nickname("test")
                .phoneNumber("010-0000-0000")
                .status("active")
                .build();

        userRepository.save(user);

        User updateUser = User.builder()
                .email("test@gmail.com")
                .password("testUpdated")
                .username("testUpdated")
                .address("testUpdated")
                .postCode("Updated")
                .age(20)
                .nickname("testUpdated")
                .phoneNumber("010-0000-0000")
                .status("active")
                .build();


        //when
        User updatedUser = userRepository.save(updateUser);

        //then
        assertThat(updatedUser).isSameAs(updateUser);
    }

    @Test
    @DisplayName("## 유저 삭제하기 레파지토리 테스트 ##")
    public void deleteUser () throws Exception{


        //given
        User user = User.builder()
                .email("test@gmail.com")
                .password("test")
                .username("test")
                .address("test")
                .postCode("test")
                .age(20)
                .nickname("test")
                .phoneNumber("010-0000-0000")
                .status("active")
                .build();

        User savedUser = userRepository.save(user);


        //when
        userRepository.delete(savedUser);
        boolean isExistUser = userRepository.existsByEmail(savedUser.getEmail());

        //then
        assertThat(isExistUser).isFalse();
    }
}