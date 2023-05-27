package com.project.shopping.controller;
import static org.mockito.ArgumentMatchers.any;

import com.project.shopping.auth.WithMockCustomUser;
import com.project.shopping.dto.responseDTO.EmailAuthenticationResponseDTO.CheckAuthCodeResponseDTO;
import com.project.shopping.dto.responseDTO.EmailAuthenticationResponseDTO.SendAuthCodeResponseDTO;
import com.project.shopping.dto.responseDTO.UserResponseDTO.*;
import com.project.shopping.model.User;
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
    void signUp() throws Exception{
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

        String json = userJoinRequestJson.toString();

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
    void deleteUser() throws Exception{

        StringBuilder userDeleteRequestJson = new StringBuilder();
        userDeleteRequestJson.append("{")
                .append("\"password\": \"password\"")
                .append("}");

            User user = User.builder()
                .email("test@gmail.com")
                .password("password")
                .username("test")
                .address("test")
                .postCode("test")
                .age(20)
                .nickname("test")
                .phoneNumber("010-0000-0000")
                .build();

        UserDeleteResponseDTO userDeleteResponseDTO = UserDeleteResponseDTO.toUserDeleteResponseDTO(user);


        //given
        BDDMockito.given(userService.delete(any(), any())).willReturn(userDeleteResponseDTO);


        //when
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.delete("/api/user/delete")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(userDeleteRequestJson.toString()));


        //then
        result.andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    @WithMockCustomUser(user = "test@gmail.com", roles = Role.ROLE_USER)
    @DisplayName("## OAuth 추가정보입력 ##")
    void oauthSignup () throws Exception{

        StringBuilder UserOAuthAddInfoRequestJson = new StringBuilder();
        UserOAuthAddInfoRequestJson.append("{")
                .append("\"address\": \"test\",")
                .append("\"postCode\": \"test\",")
                .append("\"nickname\": \"test\",")
                .append("\"age\": 20,")
                .append("\"phoneNumber\": \"010-0000-0000\"")
                .append("}");

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

        String json = UserOAuthAddInfoRequestJson.toString();

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
    void userInfo () throws Exception{

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
    void userUpdate () throws Exception{

        UserUpdateResponseDTO userUpdateResponseDTO = UserUpdateResponseDTO.builder()
                .email("test@gmail.com")
                .username("test")
                .address("test")
                .postCode("test")
                .nickname("test")
                .age(20)
                .phoneNumber("010-0000-0000")
                .build();


        StringBuilder userUpdateRequestJson = new StringBuilder();
        userUpdateRequestJson.append("{")
                .append("\"email\": \"test@gmail.com\",")
                .append("\"username\": \"test\",")
                .append("\"address\": \"test\",")
                .append("\"postCode\": \"test\",")
                .append("\"nickname\": \"test\",")
                .append("\"age\": 20,")
                .append("\"phoneNumber\": \"010-0000-0000\"")
                .append("}");

        //given
        BDDMockito.given(userService.updateUser(any(), any())).willReturn(userUpdateResponseDTO);

        String json = userUpdateRequestJson.toString();

        //when
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.put("/api/user/update")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));

        //then
        result.andExpect(MockMvcResultMatchers.status().isOk());
    }

//    @Test
//    @WithMockCustomUser(user = "test@gmail.com", roles = Role.ROLE_USER)
//    @DisplayName("## 이메일 체크 테스트 ##")
//    public void checkEmail () throws Exception{
//
//        UserCheckEmailResponseDTO userCheckEmailResponseDTO = UserCheckEmailResponseDTO.toUserCheckEmailResponseDTO("test@gmail.com");
//
//        //given
//        BDDMockito.given(userService.existsByEmail(any())).willReturn(true);
//
//        //when
//        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/api/join/email-check/{value}","test@gmail.com")
//                .with(SecurityMockMvcRequestPostProcessors.csrf())
//                .contentType(MediaType.APPLICATION_JSON));
//
//
//        //then
//        result.andExpect(MockMvcResultMatchers.status().isOk());
//
//    }

    @Test
    @WithMockCustomUser(user = "test@gmail.com", roles = Role.ROLE_USER)
    @DisplayName("## 닉네임 체크 테스트 ##")
    void checkNickname () throws Exception{

        UserCheckNicknameResponseDTO userCheckNicknameResponseDTO = UserCheckNicknameResponseDTO.createUserCheckNicknameResponseDTO("test");

        //given
        BDDMockito.given(userService.checkNickname(any())).willReturn(userCheckNicknameResponseDTO);

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
    void sendAuthCodeEmail () throws Exception{

        StringBuilder sendAuthCodeRequestJson = new StringBuilder();
        sendAuthCodeRequestJson.append("{")
                .append("\"email\": \"test@email.com\"")
                .append("}");


        SendAuthCodeResponseDTO sendAuthCodeResponseDTO = SendAuthCodeResponseDTO.toSendAuthCodeResponseDTO("test@gmail.com");

        //given
        BDDMockito.given(emailAuthenticationService.sendAuthenticationCode(any())).willReturn(sendAuthCodeResponseDTO);

        String json = sendAuthCodeRequestJson.toString();

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
    void checkAuthCodeEmail () throws Exception{

        StringBuilder checkAuthCodeRequestJson = new StringBuilder();
        checkAuthCodeRequestJson.append("{")
                .append("\"email\": \"test@email.com\",")
                .append("\"authCode\": \"test\"")
                .append("}");

        CheckAuthCodeResponseDTO checkAuthCodeResponseDTO = CheckAuthCodeResponseDTO.toCheckAuthCodeResponseDTO("test@email.com");

        //given
        BDDMockito.given(emailAuthenticationService.checkAuthCode(any())).willReturn(checkAuthCodeResponseDTO);

        String json = checkAuthCodeRequestJson.toString();

        //when
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/api/authcode/check")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));

        //then
        result.andExpect(MockMvcResultMatchers.status().isOk());

    }
}