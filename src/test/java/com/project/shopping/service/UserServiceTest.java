package com.project.shopping.service;

import com.project.shopping.dto.UserDTO;
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
    public void create () throws Exception{

        UserJoinRequestDTO userJoinRequestDTO = UserJoinRequestDTO.builder()
                .email("test@gmail.com")
                .password("test")
                .username("test")
                .address("test")
                .postCode("test")
                .age(20)
                .nickname("test")
                .phoneNumber("010-0000-0000")
                .build();

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
    public void oAuthLoginCreateUser () throws Exception{

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
    public void saveUser () throws Exception{

        UserOAuthAddInfoResponseDTO userOAuthAddInfoResponseDTO = UserOAuthAddInfoResponseDTO.builder()
                .email("test@gmail.com")
                .username("test")
                .address("test")
                .postCode("test")
                .age(20)
                .nickname("test")
                .phoneNumber("010-0000-0000")
                .build();

        UserOAuthAddInfoRequestDTO userOAuthAddInfoRequestDTO = UserOAuthAddInfoRequestDTO.builder()
                .address("test")
                .postCode("test")
                .age(20)
                .nickname("test")
                .phoneNumber("010-0000-0000")
                .build();

        String email = "typoon0820@gmail.com";

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
        given(userRepository.save(any())).willReturn(user);
        given(userRepository.findByEmail(any())).willReturn(optUser);


        //when
        UserOAuthAddInfoResponseDTO result = userService.oauthUserInfoAdd(email, userOAuthAddInfoRequestDTO);

        //then
        assertThat(result).usingRecursiveComparison().isEqualTo(userOAuthAddInfoResponseDTO);
    }

    @Test
    @DisplayName("## 이메일로 유저찾기 서비스 테스트 ##")
    public void findUserByEmail () throws Exception{

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
    public void delete () throws Exception{

        UserDeleteRequestDTO userDeleteRequestDTO = UserDeleteRequestDTO.builder()
                .password("test")
                .build();

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

        UserDeleteResponseDTO userDeleteResponseDTO = UserDeleteResponseDTO.builder()
                .email("test@gmail.com")
                .username("test")
                .nickname("test")
                .build();


        Optional<User> optUser = Optional.of(user);

        String email = "test@gmail.com";

        //given
        given(userRepository.findByEmail(any())).willReturn(optUser);
        given(userRepository.save(any())).willReturn(user);


        //when
        UserDeleteResponseDTO result = userService.delete(userDeleteRequestDTO, user);

        //then
        assertThat(result).usingRecursiveComparison().isEqualTo(userDeleteResponseDTO);
    }

    @Test
    @DisplayName("## 이메일로 유저 존재여부 확인 서비스 테스트 ##")
    public void existsByEmail () throws Exception{


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
    public void existsByNickname () throws Exception{


        String nickname = "test";

        //given
        given(userRepository.existsByNickname(any())).willReturn(true);


        //when
        boolean result = userService.existsByNickname(nickname);

        //then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("## 유저 정보 갱신 서비스 테스트 ##")
    public void updateUser () throws Exception{

        UserUpdateResponseDTO userUpdateResponseDTO = UserUpdateResponseDTO.builder()
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

        UserUpdateRequestDTO userUpdateRequestDTO = UserUpdateRequestDTO.builder()
                .email("test@gmail.com")
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
        given(userRepository.existsByNickname(any())).willReturn(false);
        given(userRepository.save(any())).willReturn(user);


        //when
        UserUpdateResponseDTO result = userService.updateUser(userUpdateRequestDTO, user);

        //then
        assertThat(result).usingRecursiveComparison().isEqualTo(userUpdateResponseDTO);
    }

    @Test
    @DisplayName("## 이메일로 유저 정보 조회 서비스 테스트 ##")
    public void findByEmail () throws Exception{


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