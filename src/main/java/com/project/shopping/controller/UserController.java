package com.project.shopping.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.shopping.auth.PrincipalDetails;
import com.project.shopping.auth.PrinciplaDetailsService;
import com.project.shopping.dto.ResponseDTO;
import com.project.shopping.dto.UserDTO;
import com.project.shopping.dto.requestDTO.UserRequestDTO.*;
import com.project.shopping.dto.requestDTO.request;
import com.project.shopping.dto.responseDTO.UserResponseDTO.*;
import com.project.shopping.model.User;
import com.project.shopping.service.UserService;
import com.sun.jdi.request.DuplicateRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpResponse;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;


@RestController
@Slf4j
public class UserController  {


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


//    @PostMapping("/api/login")
//    public ResponseEntity<?> login(@RequestBody UserLoginRequestDTO userLoginRequestDTO) throws IOException {
//        HttpHeaders headers = new HttpHeaders();
//        headers.setLocation(URI.create("/login"));
//        return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
//    }
//

    @PostMapping("/api/join")
    public ResponseEntity<?> signup(@RequestBody userJoinRequestDTO userJoinRequestDTO){
        try{
            //System.out.println(userDTO.getPassword());
            //System.out.println(passwordEncoder.encode(userDTO.getPassword()));

            if(userService.existsByEmail(userJoinRequestDTO.getEmail())){
                throw new DuplicateRequestException("이미 이메일이 존재합니다.");
            }
            if(userJoinRequestDTO.getEmail().equals("")){
                throw new NoSuchElementException("잘못된 형식의 데이터 입니다. ");
            }
            if(userJoinRequestDTO.getAddress().equals("")){
                throw new NoSuchElementException("잘못된 형식의 데이터 입니다. ");
            }
            if(userJoinRequestDTO.getAge() == 0){
                throw new NoSuchElementException("잘못된 형식의 데이터 입니다. ");
            }
            if(userJoinRequestDTO.getPassword().equals("")){
                throw new NoSuchElementException("잘못된 형식의 데이터 입니다. ");
            }
            if(userJoinRequestDTO.getUsername().equals("")){
                throw new NoSuchElementException("잘못된 형식의 데이터 입니다. ");
            }
            if(userJoinRequestDTO.getPhoneNumber().equals("")){
                throw new NoSuchElementException("잘못된 형식의 데이터 입니다. ");
            }
            if(userJoinRequestDTO.getNickname().equals("")){
                throw new NoSuchElementException("잘못된 형식의 데이터 입니다. ");
            }
            if(userJoinRequestDTO.getPostCode().equals("")){
                throw new NoSuchElementException("잘못된 형식의 데이터 입니다. ");
            }


            User user = User.builder()
                    .email(userJoinRequestDTO.getEmail())
                    .password(passwordEncoder.encode((userJoinRequestDTO.getPassword())))
                    .username(userJoinRequestDTO.getUsername())
                    .address(userJoinRequestDTO.getAddress()).age(userJoinRequestDTO.getAge())
                    .roles("ROLE_USER")
                    .nickname(userJoinRequestDTO.getNickname()).phoneNumber(userJoinRequestDTO.getPhoneNumber())
                    .status("active")
                    .createDate(Timestamp.valueOf(LocalDateTime.now()))
                    .modifieddate(Timestamp.valueOf(LocalDateTime.now()))
                    .postCode(userJoinRequestDTO.getPostCode()).build();
            User registeredUser = userService.create(user);

            UserJoinResponseDTO userJoinResponseDTO = UserJoinResponseDTO.builder()
                    .email(registeredUser.getEmail())
                    .username(registeredUser.getUsername())
                    .address(registeredUser.getAddress())
                    .postCode(registeredUser.getPostCode())
                    .age(registeredUser.getAge())
                    .nickname(registeredUser.getNickname())
                    .phoneNumber(registeredUser.getPhoneNumber())
                    .createDate(registeredUser.getCreateDate())
                    .modifiedDate(registeredUser.getModifieddate())
                    .build();
            Map<String, Object> response = new HashMap<>();
            response.put("msg", "회원가입에 성공했습니다.");
            response.put("data", userJoinResponseDTO);

            return ResponseEntity.ok().body(response);
        }catch (Exception e){

            ResponseDTO response = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/api/user/delete")
    public ResponseEntity<?> deleteUser(@RequestBody UserDeleteRequestDTO userDeleteRequestDTO, Authentication authentication ){
        try{
            PrincipalDetails userDetails = (PrincipalDetails) authentication.getPrincipal();
            String email = userDetails.getUser().getEmail();
            User user = userService.findEmailByUser(email);
            System.out.println(user.getPassword());
            System.out.println(userDeleteRequestDTO.getPassword());
            System.out.println(passwordEncoder.encode(userDetails.getPassword()));
            System.out.println(passwordEncoder.encode("abc"));
            System.out.println(passwordEncoder.encode("abc"));

            if(passwordEncoder.matches(userDeleteRequestDTO.getPassword(), userDetails.getPassword() )){

                user.setStatus("Disable");
                userService.updateUser(user);
            }else{
                throw  new NoSuchMethodException("비밀번호가 틀렸습니다.");
            }
            UserDeleteResponseDTO userDeleteResponseDTO = UserDeleteResponseDTO.
                    builder().
                    email(user.getEmail())
                    .username(user.getUsername())
                    .nickname(user.getNickname())
                    .build();
            Map<String, Object> response = new HashMap<>();
            response.put("msg", "회원탈퇴에 성공했습니다.");
            response.put("data", userDeleteResponseDTO);
            return  ResponseEntity.ok().body(response);


        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());

        }



    }

    @PostMapping("/Oauth/join")
    public ResponseEntity<?> oauthsignup(@RequestBody UserDTO userDTO,Authentication authentication) {
        PrincipalDetails userDetails = (PrincipalDetails) authentication.getPrincipal();
        String email = userDetails.getUser().getEmail();
        User user = userService.findEmailByUser(email);
        user.setAddress(userDTO.getAddress());
        user.setAge(userDTO.getAge());
        user.setPostCode(userDTO.getPostCode());
        user.setNickname(userDTO.getNickname());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        userService.SaveUser(user);

        UserDTO response = UserDTO.builder().username(user.getUsername()).email(user.getEmail())
                .age(user.getAge()).address(user.getAddress())
                .nickname(user.getNickname()).phoneNumber(user.getPhoneNumber()).build();
        return ResponseEntity.ok().body(response);


    }


//    @PostMapping("/Oauth/join")
//    public ResponseEntity<?> oauthsignup(@RequestBody UserDTO userDTO) {
//
//        try{
//            //System.out.println(userDTO.getPassword());
//            //System.out.println(passwordEncoder.encode(userDTO.getPassword()));
//            String password = passwordEncoder.encode(userDTO.getEmail());
//            User user = User.builder()
//                    .email(userDTO.getEmail())
//                    .password(password)
//                    .username(userDTO.getUsername())
//                    .address(userDTO.getAddress()).age(userDTO.getAge())
//                    .roles("ROLE_USER")
//                    .nickname(userDTO.getNickname()).phoneNumber(userDTO.getPhoneNumber())
//                    .createDate(Timestamp.valueOf(LocalDateTime.now()))
//                    .status("active")
//                    .postCode(userDTO.getPostCode()).build();
//            User registeredUser = userService.create(user);
//
//            UserDTO response = UserDTO.builder().username(registeredUser.getUsername()).email(registeredUser.getEmail())
//                    .age(registeredUser.getAge()).address(user.getAddress())
//                    .nickname(registeredUser.getNickname()).phoneNumber(registeredUser.getPhoneNumber())
//                    .postCode(registeredUser.getPostCode()).build();
//            return ResponseEntity.ok().body(response);
//        }catch (Exception e){
//            ResponseDTO response = ResponseDTO.builder().error(e.getMessage()).build();
//            return ResponseEntity.badRequest().body(response);
//        }
//
//
//    }

    @GetMapping("/api/user/info")
    public ResponseEntity<?> userinfo(Authentication authentication){
        try{
            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
            String email = principalDetails.getUser().getEmail();

            User user = userService.findEmailByUser(email); // 유저 찾기

            UserInfoResponseDTO userInfoResponseDTO = UserInfoResponseDTO.builder()
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .age(user.getAge())
                    .address(user.getAddress())
                    .nickname(user.getNickname())
                    .phoneNumber(user.getPhoneNumber())
                    .postCode(user.getPostCode())
                    .createDate(user.getCreateDate())
                    .modifiedDate(user.getModifieddate())
                    .build();
            Map<String, Object> response = new HashMap<>();
            response.put("msg", "유저조회에 성공했습니다.");
            response.put("data", userInfoResponseDTO);
            return ResponseEntity.ok().body(response);
        }catch (Exception e){
            return ResponseEntity.ok().body(e.getMessage());
        }
    }

    @PutMapping("/api/user/update")
    public ResponseEntity<?> userupdate(Authentication authentication, @RequestBody UserUpdateRequestDTO userUpdateRequestDTO){
        try{
            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
            String email = principalDetails.getUser().getEmail();
            User user = userService.findEmailByUser(email); // 해당 user찾기
            if(userUpdateRequestDTO.getEmail()!=""){
                user.setEmail(userUpdateRequestDTO.getEmail());
            }
            if(userUpdateRequestDTO.getUsername()!= ""){
                user.setUsername(userUpdateRequestDTO.getUsername());
            }
            if(userUpdateRequestDTO.getAddress() != ""){
                user.setAddress(userUpdateRequestDTO.getAddress());
            }
            if (userUpdateRequestDTO.getAge() != 0){
                user.setAge(userUpdateRequestDTO.getAge());
            }

            if(userUpdateRequestDTO.getPostCode() != "")
                user.setPostCode(userUpdateRequestDTO.getPostCode());

            if(userUpdateRequestDTO.getNickname() != ""){
                user.setNickname(userUpdateRequestDTO.getNickname());
            }
            if(userUpdateRequestDTO.getPhoneNumber()!= ""){
                user.setPhoneNumber(userUpdateRequestDTO.getPhoneNumber());
            }

            user.setModifieddate(Timestamp.valueOf(LocalDateTime.now()));

            User updateUser = userService.updateUser(user);
            UserUpdateResponseDTO userUpdateResponseDTO = UserUpdateResponseDTO.builder()
                    .username(updateUser.getUsername())
                    .email(updateUser.getEmail())
                    .age(updateUser.getAge())
                    .address(updateUser.getAddress())
                    .nickname(updateUser.getNickname())
                    .phoneNumber(updateUser.getPhoneNumber())
                    .postCode(updateUser.getPostCode())
                    .createDate(updateUser.getCreateDate())
                    .modifiedDate(updateUser.getModifieddate())
                    .build();
            Map<String, Object> response = new HashMap<>();
            response.put("msg", "유저수정에 성공했습니다.");
            response.put("data", userUpdateResponseDTO);
            return  ResponseEntity.ok().body(response);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @GetMapping("/api/join/email-check")
    public ResponseEntity<?> checkemail(@RequestBody UserCheckEmailRequestDTO userCheckEmailDTO){
        try{
            // 존재할시
            if(userService.existsByEmail(userCheckEmailDTO.getEmail())){
                throw  new DuplicateRequestException("이메일이 존재합니다.");
            }
            UserCheckEmailResponseDTO userCheckEmailResponseDTO = UserCheckEmailResponseDTO.builder().email(userCheckEmailDTO.getEmail()).build();

            Map<String, Object> response = new HashMap<>();
            response.put("msg", "사용가능한 이메일 입니다..");
            response.put("data", userCheckEmailResponseDTO);
            return ResponseEntity.ok().body(response);
        }catch (Exception e){
            return  ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/api/join/nickname-check")
    public ResponseEntity<?> checknickname(@RequestBody UserCheckNicknameRequestDTO userCheckNicknameRequestDTO){
        try{
            if(userService.existsByNickname(userCheckNicknameRequestDTO.getNickname())){
                throw  new DuplicateRequestException("닉네임이 존재합니다.");
            }
            UserCheckNicknameResponseDTO userCheckNicknameResponseDTO = UserCheckNicknameResponseDTO.builder().nickname(userCheckNicknameRequestDTO.getNickname()).build();
            Map<String, Object> response = new HashMap<>();
            response.put("msg", "사용가능한 이메일 입니다..");
            response.put("data", userCheckNicknameResponseDTO);
            return ResponseEntity.ok().body(response);
        }catch (Exception e){
            return  ResponseEntity.badRequest().body(e.getMessage());
        }

    }
}
