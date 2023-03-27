package com.project.shopping.controller;


import com.project.shopping.auth.PrincipalDetails;

import com.project.shopping.dto.UserDTO;
import com.project.shopping.dto.requestDTO.EmailAuthenticationRequestDTO.CheckAuthCodeRequestDTO;
import com.project.shopping.dto.requestDTO.EmailAuthenticationRequestDTO.SendAuthCodeRequestDTO;
import com.project.shopping.dto.requestDTO.UserRequestDTO.*;

import com.project.shopping.dto.responseDTO.EmailAuthenticationResponseDTO.CheckAuthCodeResponseDTO;
import com.project.shopping.dto.responseDTO.EmailAuthenticationResponseDTO.SendAuthCodeResponseDTO;
import com.project.shopping.dto.responseDTO.UserResponseDTO.*;
import com.project.shopping.model.User;
import com.project.shopping.service.EmailAuthenticationService;
import com.project.shopping.service.UserService;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;


@RestController
@Slf4j

@RequiredArgsConstructor
public class UserController  {


    private final  UserService userService;
    private final EmailAuthenticationService emailAuthenticationService;

    @PostMapping("/api/join")
    public ResponseEntity<?> signup(@RequestBody @Valid UserJoinRequestDTO userJoinRequestDTO){

        UserJoinResponseDTO userJoinResponseDTO = userService.create(userJoinRequestDTO);

        Map<String, Object> response = new HashMap<>();
        response.put("msg", "회원가입에 성공했습니다.");
        response.put("data", userJoinResponseDTO);

        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/api/user/delete")
    public ResponseEntity<?> deleteUser(@RequestBody @Valid UserDeleteRequestDTO userDeleteRequestDTO, Authentication authentication ){

        User user = ((PrincipalDetails) authentication.getPrincipal()).getUser();

        UserDeleteResponseDTO userDeleteResponseDTO  = userService.delete(userDeleteRequestDTO, user);

        Map<String, Object> response = new HashMap<>();
        response.put("msg", "회원탈퇴에 성공했습니다.");
        response.put("data", userDeleteResponseDTO);

        return  ResponseEntity.ok().body(response);
    }

    @PostMapping("/api/oauth/user/info/add")
    public ResponseEntity<?> oauthUserInfoAdd(Authentication authentication, @RequestBody UserOAuthAddInfoRequestDTO userOAuthAddInfoRequestDTO) {

        String email = ((PrincipalDetails) authentication.getPrincipal()).getEmail();

        return ResponseEntity.ok().body( userService.oauthUserInfoAdd(email,userOAuthAddInfoRequestDTO));
    }



    @GetMapping("/api/user/info")
    public ResponseEntity<?> userinfo(Authentication authentication){

        String email = ((PrincipalDetails)authentication.getPrincipal()).getEmail();

        UserInfoResponseDTO userInfoResponseDTO = userService.findUserByEmail(email); // 유저 찾기

        Map<String, Object> response = new HashMap<>();
        response.put("msg", "유저조회에 성공했습니다.");
        response.put("data", userInfoResponseDTO);
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/api/user/update")
    public ResponseEntity<?> userUpdate(Authentication authentication, @RequestBody @Valid UserUpdateRequestDTO userUpdateRequestDTO){

        User user = ((PrincipalDetails) authentication.getPrincipal()).getUser();

        UserUpdateResponseDTO userUpdateResponseDTO = userService.updateUser(userUpdateRequestDTO, user);

        Map<String, Object> response = new HashMap<>();
        response.put("msg", "유저수정에 성공했습니다.");
        response.put("data", userUpdateResponseDTO);
        return  ResponseEntity.ok().body(response);
    }


    @GetMapping("/api/join/email-check/{value}")
    public ResponseEntity<?> checkEmail(@PathVariable(value = "value") String value) throws Exception {
        userService.existsByEmail(value);
        UserCheckEmailResponseDTO userCheckEmailResponseDTO = UserCheckEmailResponseDTO.builder().email(value).build();

        Map<String, Object> response = new HashMap<>();
        response.put("msg", "사용가능한 이메일 입니다.");
        response.put("data", userCheckEmailResponseDTO);
        return ResponseEntity.ok().body(response);

    }

    @GetMapping("/api/join/nickname-check/{value}")
    public ResponseEntity<?> checkNickname(@PathVariable(value = "value") String value){
        userService.existsByNickname(value);
        UserCheckNicknameResponseDTO userCheckNicknameResponseDTO = UserCheckNicknameResponseDTO.builder().nickname(value).build();
        Map<String, Object> response = new HashMap<>();
        response.put("msg", "사용가능한 닉네임 입니다.");
        response.put("data", userCheckNicknameResponseDTO);
        return ResponseEntity.ok().body(response);

    }

    @PostMapping("/api/authcode/email")
    public ResponseEntity<?> sendAuthCodeEmail(@RequestBody @Valid SendAuthCodeRequestDTO sendAuthCodeRequestDTO) throws MessagingException, UnsupportedEncodingException {
        SendAuthCodeResponseDTO sendAuthCodeResponseDTO = emailAuthenticationService.sendAuthenticationCode(sendAuthCodeRequestDTO);

        Map<String, Object> response = new HashMap<>();
        response.put("msg", "이메일 전송이 완료되었습니다.");
        response.put("data", sendAuthCodeResponseDTO);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/api/authcode/check")
    public ResponseEntity<?> checkAuthCodeEmail(@RequestBody @Valid CheckAuthCodeRequestDTO checkAuthCodeRequestDTO){

        CheckAuthCodeResponseDTO checkAuthCodeResponseDTO = emailAuthenticationService.checkAuthCode(checkAuthCodeRequestDTO);

        Map<String, Object> response = new HashMap<>();
        response.put("msg", "이메일 인증이 완료되었습니다.");
        response.put("data", checkAuthCodeResponseDTO);
        return ResponseEntity.ok().body(response);
    }
}
