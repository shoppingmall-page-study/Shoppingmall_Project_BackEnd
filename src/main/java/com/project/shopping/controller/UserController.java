package com.project.shopping.controller;

import com.project.shopping.dto.ResponseDTO;
import com.project.shopping.dto.UserDTO;
import com.project.shopping.model.User;
import com.project.shopping.security.Tokenprovider;
import com.project.shopping.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequestMapping("/auth")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private Tokenprovider tokenprovider;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody UserDTO userDTO){
        try{
            User user = User.builder()
                    .email(userDTO.getEmail())
                    .password(passwordEncoder.encode((userDTO.getPassword())))
                    .username(userDTO.getUsername())
                    .address(userDTO.getAddress()).age(userDTO.getAge())
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

    @PostMapping("signin")
    public ResponseEntity<?> signin(@RequestBody UserDTO userDTO){
        User user = userService.getByCredentials(userDTO.getEmail(), userDTO.getPassword(), passwordEncoder);
        if(user != null){
            final String token = tokenprovider.create(user);
            final UserDTO response = UserDTO.builder().email(user.getEmail())
                    .phoneNumber(user.getPhoneNumber())
                    .nickname(user.getNickname())
                    .username(user.getUsername())
                    .address(user.getAddress())
                    .age(user.getAge())
                    .token(token).build();
            return ResponseEntity.ok().body(response);
        }else{
            ResponseDTO response = ResponseDTO.builder().error("login failed").build();
            return ResponseEntity.badRequest().body(response);
        }


    }

}
