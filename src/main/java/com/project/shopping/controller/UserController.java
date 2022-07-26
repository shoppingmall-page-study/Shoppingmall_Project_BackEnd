package com.project.shopping.controller;

import com.project.shopping.auth.PrincipalDetails;
import com.project.shopping.dto.ResponseDTO;
import com.project.shopping.dto.UserDTO;
import com.project.shopping.model.User;
import com.project.shopping.security.Tokenprovider;
import com.project.shopping.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;
//    @Autowired
//    private Tokenprovider tokenprovider;

//    @PostMapping("join")
//    public String join(@RequestBody User user){
//
//        return "회원 가입 완료 ";
//    }

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;


    @PostMapping("/join")
    public ResponseEntity<?> signup(@RequestBody UserDTO userDTO){
        try{
            //System.out.println(userDTO.getPassword());
            //System.out.println(passwordEncoder.encode(userDTO.getPassword()));
            User user = User.builder()
                    .email(userDTO.getEmail())
                    .password(passwordEncoder.encode((userDTO.getPassword())))
                    .username(userDTO.getUsername())
                    .address(userDTO.getAddress()).age(userDTO.getAge())
                    .roles("ROLE_USER")
                    .nickname(userDTO.getNickname()).phoneNumber(userDTO.getPhoneNumber()).build();
            User registeredUser = userService.create(user);

            UserDTO response = UserDTO.builder().username(registeredUser.getUsername()).email(registeredUser.getEmail())
                    .age(registeredUser.getAge()).address(user.getAddress())
                    .nickname(registeredUser.getNickname()).phoneNumber(registeredUser.getPhoneNumber()).build();
            return ResponseEntity.ok().body(response);
        }catch (Exception e){
            ResponseDTO response = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity.badRequest().body(response);
        }
    }


    @PostMapping("/Oauth/join")
    public ResponseEntity<?> oauthsignup(@RequestBody UserDTO userDTO,Authentication authentication) {
        PrincipalDetails userDetails = (PrincipalDetails) authentication.getPrincipal();
        String email = userDetails.getUser().getEmail();
        User user = userService.findEmailByUser(email);
        user.setAddress(userDTO.getAddress());
        user.setAge(userDTO.getAge());
        user.setNickname(userDTO.getNickname());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        userService.SaveUser(user);

        UserDTO response = UserDTO.builder().username(user.getUsername()).email(user.getEmail())
                .age(user.getAge()).address(user.getAddress())
                .nickname(user.getNickname()).phoneNumber(user.getPhoneNumber()).build();
        return ResponseEntity.ok().body(response);


    }






}
