package com.project.shopping.controller;
import static org.mockito.ArgumentMatchers.any;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.shopping.auth.WithMockCustomUser;
import com.project.shopping.dto.requestDTO.EmailAuthenticationRequestDTO.CheckAuthCodeRequestDTO;
import com.project.shopping.dto.requestDTO.EmailAuthenticationRequestDTO.SendAuthCodeRequestDTO;
import com.project.shopping.dto.requestDTO.UserRequestDTO.UserDeleteRequestDTO;
import com.project.shopping.dto.requestDTO.UserRequestDTO.UserJoinRequestDTO;
import com.project.shopping.dto.requestDTO.UserRequestDTO.UserOAuthAddInfoRequestDTO;
import com.project.shopping.dto.requestDTO.UserRequestDTO.UserUpdateRequestDTO;
import com.project.shopping.dto.responseDTO.EmailAuthenticationResponseDTO.CheckAuthCodeResponseDTO;
import com.project.shopping.dto.responseDTO.EmailAuthenticationResponseDTO.SendAuthCodeResponseDTO;
import com.project.shopping.dto.responseDTO.UserResponseDTO.*;
import com.project.shopping.security.Role;
import com.project.shopping.service.EmailAuthenticationService;
import com.project.shopping.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(controllers = UserController.class,
        excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurerAdapter.class)})
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private EmailAuthenticationService emailAuthenticationService;

    @Test
    @WithMockCustomUser(user = "test@gmail.com", roles = Role.ROLE_USER)
    @DisplayName("## 회원가입 테스트 ##")
    public void signUp() throws Exception{

        UserJoinRequestDTO userJoinRequestDTO = UserJoinRequestDTO.builder()
                .email("test@gmail.com")
                .password("password")
                .username("test")
                .address("test")
                .postCode("test")
                .nickname("test")
                .age(20)
                .phoneNumber("010-0000-0000")
                .build();

        UserJoinResponseDTO userJoinResponseDTO = UserJoinResponseDTO.builder()
                .email("test@gmail.com")
                .username("test")
                .address("test")
                .postCode("test")
                .nickname("test")
                .age(20)
                .phoneNumber("010-0000-0000")
                .build();

        //given
        BDDMockito.given(userService.create(any())).willReturn(userJoinResponseDTO);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(userJoinRequestDTO);

        //when
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/api/join")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));


        //then
        result.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockCustomUser(user = "test@gmail.com", roles = Role.ROLE_USER)
    @DisplayName("## 유저 삭제 테스트 ##")
    public void deleteUser() throws Exception{

        UserDeleteRequestDTO userDeleteRequestDTO = UserDeleteRequestDTO.builder()
                .password("password")
                .build();

        UserDeleteResponseDTO userDeleteResponseDTO = UserDeleteResponseDTO.toUserDeleteResponseDTO("test@gmail.com", "test", "test");


        //given
        BDDMockito.given(userService.delete(any(), any())).willReturn(userDeleteResponseDTO);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(userDeleteRequestDTO);

        //when
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.delete("/api/user/delete")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));


        //then
        result.andExpect(MockMvcResultMatchers.status().isOk());

    }
    
    @Test
    @WithMockCustomUser(user = "test@gmail.com", roles = Role.ROLE_USER)
    @DisplayName("## OAuth 추가정보입력 ##")
    public void oauthSignup () throws Exception{

        UserOAuthAddInfoRequestDTO userOAuthAddInfoRequestDTO = UserOAuthAddInfoRequestDTO.builder()
                .address("test")
                .postCode("test")
                .nickname("test")
                .age(20)
                .phoneNumber("010-0000-0000")
                .build();

        UserOAuthAddInfoResponseDTO userOAuthAddInfoResponseDTO = UserOAuthAddInfoResponseDTO.builder()
                .email("test@gmail.com")
                .username("test")
                .address("test")
                .postCode("test")
                .nickname("test")
                .age(20)
                .phoneNumber("010-0000-0000")
                .build();

        //given
        BDDMockito.given(userService.oauthUserInfoAdd(any(),any())).willReturn(userOAuthAddInfoResponseDTO);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(userOAuthAddInfoRequestDTO);

        //when
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/api/oauth/user/info/add")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));

        //then
        result.andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    @WithMockCustomUser(user = "test@gmail.com", roles = Role.ROLE_USER)
    @DisplayName("## 유저정보가져오기 ##")
    public void userInfo () throws Exception{

        UserInfoResponseDTO userInfoResponseDTO = UserInfoResponseDTO.builder()
                .email("test@gmail.com")
                .username("test")
                .address("test")
                .postCode("test")
                .nickname("test")
                .age(20)
                .phoneNumber("010-0000-0000")
                .build();

        //given
        BDDMockito.given(userService.findUserByEmail(any())).willReturn(userInfoResponseDTO);

        //when
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/api/user/info")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON));

        //then
        result.andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    @WithMockCustomUser(user = "test@gmail.com", roles = Role.ROLE_USER)
    @DisplayName("## 유저 업데이트 테스트 ##")
    public void userUpdate () throws Exception{

        UserUpdateResponseDTO userUpdateResponseDTO = UserUpdateResponseDTO.builder()
                .email("test@gmail.com")
                .username("test")
                .address("test")
                .postCode("test")
                .nickname("test")
                .age(20)
                .phoneNumber("010-0000-0000")
                .build();

        UserUpdateRequestDTO userUpdateRequestDTO = UserUpdateRequestDTO.builder()
                .email("test@gmail.com")
                .username("test")
                .address("test")
                .postCode("test")
                .nickname("test")
                .age(20)
                .phoneNumber("010-0000-0000")
                .build();

        //given
        BDDMockito.given(userService.updateUser(any(), any())).willReturn(userUpdateResponseDTO);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(userUpdateRequestDTO);

        //when
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.put("/api/user/update")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));

        //then
        result.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockCustomUser(user = "test@gmail.com", roles = Role.ROLE_USER)
    @DisplayName("## 이메일 체크 테스트 ##")
    public void checkEmail () throws Exception{

        UserCheckEmailResponseDTO userCheckEmailResponseDTO = UserCheckEmailResponseDTO.toUserCheckEmailResponseDTO("test@gmail.com");

        //given
        BDDMockito.given(userService.existsByEmail(any())).willReturn(true);

        //when
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/api/join/email-check/{value}","test@gmail.com")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON));


        //then
        result.andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    @WithMockCustomUser(user = "test@gmail.com", roles = Role.ROLE_USER)
    @DisplayName("## 닉네임 체크 테스트 ##")
    public void checkNickname () throws Exception{

        UserCheckNicknameResponseDTO userCheckNicknameResponseDTO = UserCheckNicknameResponseDTO.createUserCheckNicknameResponseDTO("test");

        //given
        BDDMockito.given(userService.existsByEmail(any())).willReturn(true);

        //when
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/api/join/nickname-check/{value}","test")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON));

        //then
        result.andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    @WithMockCustomUser(user = "test@gmail.com", roles = Role.ROLE_USER)
    @DisplayName("## 인증코드 이메일 전송 테스트 ##")
    public void sendAuthCodeEmail () throws Exception{

        SendAuthCodeRequestDTO sendAuthCodeRequestDTO = SendAuthCodeRequestDTO.builder()
                .email("test@email.com")
                .build();

        SendAuthCodeResponseDTO sendAuthCodeResponseDTO = SendAuthCodeResponseDTO.toSendAuthCodeResponseDTO("test@gmail.com");

        //given
        BDDMockito.given(emailAuthenticationService.sendAuthenticationCode(any())).willReturn(sendAuthCodeResponseDTO);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(sendAuthCodeRequestDTO);

        //when
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/api/authcode/email")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));

        //then
        result.andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    @WithMockCustomUser(user = "test@gmail.com", roles = Role.ROLE_USER)
    @DisplayName("## 인증코드 이메일 확인 테스트 ##")
    public void checkAuthCodeEmail () throws Exception{

        CheckAuthCodeRequestDTO checkAuthCodeRequestDTO = CheckAuthCodeRequestDTO.builder()
                .email("test@email.com")
                .authCode("test")
                .build();

        CheckAuthCodeResponseDTO checkAuthCodeResponseDTO = CheckAuthCodeResponseDTO.toCheckAuthCodeResponseDTO("test@email.com");

        //giveni
        BDDMockito.given(emailAuthenticationService.checkAuthCode(any())).willReturn(checkAuthCodeResponseDTO);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(checkAuthCodeRequestDTO);

        //when
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/api/authcode/check")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));

        //then
        result.andExpect(MockMvcResultMatchers.status().isOk());

    }
}