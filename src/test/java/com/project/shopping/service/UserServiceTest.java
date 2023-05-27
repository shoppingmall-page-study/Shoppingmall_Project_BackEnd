package com.project.shopping.service;

import com.google.gson.Gson;
import com.project.shopping.dto.requestDTO.UserRequestDTO.UserDeleteRequestDTO;
import com.project.shopping.dto.requestDTO.UserRequestDTO.UserJoinRequestDTO;
import com.project.shopping.dto.requestDTO.UserRequestDTO.UserOAuthAddInfoRequestDTO;
import com.project.shopping.dto.requestDTO.UserRequestDTO.UserUpdateRequestDTO;
import com.project.shopping.dto.responseDTO.UserResponseDTO.*;
import com.project.shopping.model.User;
import com.project.shopping.repository.UserRepository;
import com.project.shopping.security.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Spy
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private EmailAuthenticationService emailAuthenticationService;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("## 유저 생성 서비스 테스트 ##")
    public void create (){

        StringBuilder userJoinRequestJson = new StringBuilder();
        userJoinRequestJson.append("{")
                .append("\"email\": \"test@gmail.com\",")
                .append("\"password\": \"password\",")
                .append("\"username\": \"test\",")
                .append("\"address\": \"test\",")
                .append("\"postCode\": \"test\",")
                .append("\"nickname\": \"test\",")
                .append("\"age\": 20,")
                .append("\"phoneNumber\": \"010-0000-0000\"")
                .append("}");

        UserJoinRequestDTO userJoinRequestDTO = new Gson().fromJson(userJoinRequestJson.toString(), UserJoinRequestDTO.class);


        UserJoinResponseDTO userJoinResponseDTO = UserJoinResponseDTO.builder()
                .email("test@gmail.com")
                .username("test")
                .address("test")
                .postCode("test")
                .age(20)
                .nickname("test")
                .phoneNumber("010-0000-0000")
                .build();

        User user = User.builder()
                .email("test@gmail.com")
                .password("test")
                .username("test")
                .address("test")
                .postCode("test")
                .age(20)
                .nickname("test")
                .phoneNumber("010-0000-0000")
                .build();

        //given
        given(userRepository.existsByEmail(any())).willReturn(false);
        given(emailAuthenticationService.isEmailAuthenticated(any())).willReturn(true);
        given(userRepository.existsByNickname(any())).willReturn(false);
        given(userRepository.save(any())).willReturn(user);

        //when
        UserJoinResponseDTO result = userService.create(userJoinRequestDTO);

        //then
        assertThat(result).usingRecursiveComparison().isEqualTo(userJoinResponseDTO);
    }


    @Test
    @DisplayName("## OAuth 유저 초기 생성 서비스 테스트 ##")
    void oAuthLoginCreateUser(){

        String email = "test@gmail.com";
        String name = "test";

        User user = User.builder()
                .email(email)
                .password("test")
                .username(name)
                .address("null").age(1000)
                .roles(Role.ROLE_USER)
                .status("null")
                .postCode("null")
                .nickname("null").phoneNumber("null").build();


        //given
        given(userRepository.save(any())).willReturn(user);

        //when
        User result = userService.oAuthLoginCreateUser(email, name);

        //then
        assertThat(result).usingRecursiveComparison().isEqualTo(user);
    }


    @Test
    @DisplayName("## OAuth 유저 추가정보 입력 서비스 테스트 ##")
    public void saveUser (){

        StringBuilder UserOAuthAddInfoRequestJson = new StringBuilder();
        UserOAuthAddInfoRequestJson.append("{")
                .append("\"address\": \"test\",")
                .append("\"postCode\": \"test\",")
                .append("\"nickname\": \"test\",")
                .append("\"age\": 20,")
                .append("\"phoneNumber\": \"010-0000-0000\"")
                .append("}");

        UserOAuthAddInfoRequestDTO userOAuthAddInfoRequestDTO = new Gson().fromJson(UserOAuthAddInfoRequestJson.toString(), UserOAuthAddInfoRequestDTO.class);

        UserOAuthAddInfoResponseDTO userOAuthAddInfoResponseDTO = UserOAuthAddInfoResponseDTO.builder()
                .email("test@gmail.com")
                .username("test")
                .address("test")
                .postCode("test")
                .age(20)
                .nickname("test")
                .phoneNumber("010-0000-0000")
                .build();

        User user = User.builder()
                .email("test@gmail.com")
                .password("test")
                .username("test")
                .address("test")
                .postCode("test")
                .age(20)
                .nickname("test")
                .phoneNumber("010-0000-0000")
                .build();

        //given
        given(userRepository.save(any())).willReturn(user);
        given(userRepository.existsByNickname(any())).willReturn(false);


        //when
        UserOAuthAddInfoResponseDTO result = userService.oauthUserInfoAdd(user, userOAuthAddInfoRequestDTO);

        //then
        assertThat(result).usingRecursiveComparison().isEqualTo(userOAuthAddInfoResponseDTO);
    }

    @Test
    @DisplayName("## 이메일로 유저찾기 서비스 테스트 ##")
    public void findUserByEmail (){

        UserInfoResponseDTO userInfoResponseDTO = UserInfoResponseDTO.builder()
                .email("test@gmail.com")
                .username("test")
                .address("test")
                .postCode("test")
                .age(20)
                .nickname("test")
                .phoneNumber("010-0000-0000")
                .build();


        User user = User.builder()
                .email("test@gmail.com")
                .password("test")
                .username("test")
                .address("test")
                .postCode("test")
                .age(20)
                .nickname("test")
                .phoneNumber("010-0000-0000")
                .build();

        Optional<User> optUser = Optional.of(user);

        String email = "test@gmail.com";

        //given
        given(userRepository.findByEmail(any())).willReturn(optUser);


        //when
        UserInfoResponseDTO result = userService.findUserByEmail(email);

        //then
        assertThat(result).usingRecursiveComparison().isEqualTo(userInfoResponseDTO);
    }

    @Test
    @DisplayName("## 유저 삭제 서비스 테스트 ##")
    void delete (){


        StringBuilder userDeleteRequestJson = new StringBuilder();
        userDeleteRequestJson.append("{")
                .append("\"password\": \"test\"")
                .append("}");

        UserDeleteRequestDTO userDeleteRequestDTO = new Gson().fromJson(userDeleteRequestJson.toString(), UserDeleteRequestDTO.class);

        User user = User.builder()
                .email("test@gmail.com")
                .password(passwordEncoder.encode("test"))
                .username("test")
                .address("test")
                .postCode("test")
                .age(20)
                .nickname("test")
                .phoneNumber("010-0000-0000")
                .build();

        UserDeleteResponseDTO userDeleteResponseDTO = UserDeleteResponseDTO.toUserDeleteResponseDTO(user);

        Optional<User> optUser = Optional.of(user);

        String email = "test@gmail.com";

        //given
        given(userRepository.existsByEmail(any())).willReturn(true);
        given(userRepository.save(any())).willReturn(user);


        //when
        UserDeleteResponseDTO result = userService.delete(userDeleteRequestDTO, user);

        //then
        assertThat(result).usingRecursiveComparison().isEqualTo(userDeleteResponseDTO);
    }

    @Test
    @DisplayName("## 이메일로 유저 존재여부 확인 서비스 테스트 ##")
    void existsByEmail (){


        String email = "test@gmail.com";

        //given
        given(userRepository.existsByEmail(any())).willReturn(true);


        //when
        boolean result = userService.existsByEmail(email);

        //then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("## 닉네임으로 유저 존재여부 확인 서비스 테스트 ##")
    void existsByNickname (){


        String nickname = "test";

        //given
        given(userRepository.existsByNickname(any())).willReturn(true);


        //when
        boolean result = userService.existsByNickname(nickname);

        //then
        assertThat(result).isTrue();
    }

//    @Test
//    @DisplayName("## 유저 정보 갱신 서비스 테스트 ##")
//    public void updateUser () throws Exception{
//
//        UserUpdateResponseDTO userUpdateResponseDTO = UserUpdateResponseDTO.builder()
//                .email("test@gmail.com")
//                .username("test")
//                .address("test")
//                .postCode("test")
//                .age(20)
//                .nickname("test")
//                .phoneNumber("010-0000-0000")
//                .build();
//
//        User user = User.builder()
//                .email("test@gmail.com")
//                .password("test")
//                .username("test")
//                .address("test")
//                .postCode("test")
//                .age(20)
//                .nickname("test")
//                .phoneNumber("010-0000-0000")
//                .build();
//
//        StringBuilder userUpdateRequestJson = new StringBuilder();
//        userUpdateRequestJson.append("{")
//                .append("\"email\": \"test@gmail.com\",")
//                .append("\"username\": \"test\",")
//                .append("\"address\": \"test\",")
//                .append("\"postCode\": \"test\",")
//                .append("\"nickname\": \"test\",")
//                .append("\"age\": 20,")
//                .append("\"phoneNumber\": \"010-0000-0000\"")
//                .append("}");
//
//
//        UserUpdateRequestDTO userUpdateRequestDTO = new Gson().fromJson(userUpdateRequestJson.toString(), UserUpdateRequestDTO.class);
//
//        Optional<User> optUser = Optional.of(user);
//
//        //given
//        given(userRepository.existsByNickname(any())).willReturn(false);
//        given(userRepository.save(any())).willReturn(user);
//
//
//        //when
//        UserUpdateResponseDTO result = userService.updateUser(userUpdateRequestDTO, user);
//
//        //then
//        assertThat(result).usingRecursiveComparison().isEqualTo(userUpdateResponseDTO);
//    }
//
    @Test
    @DisplayName("## 이메일로 유저 정보 조회 서비스 테스트 ##")
    void findByEmail (){


        String email = "test@gmail.com";

        User user = User.builder()
                .email("test@gmail.com")
                .password("test")
                .username("test")
                .address("test")
                .postCode("test")
                .age(20)
                .nickname("test")
                .phoneNumber("010-0000-0000")
                .build();

        Optional<User> optUser = Optional.of(user);
        //given
        given(userRepository.findByEmail(any())).willReturn(optUser);


        //when
        User result = userService.findByEmail(email);

        //then
        assertThat(result).usingRecursiveComparison().isEqualTo(user);
    }

}