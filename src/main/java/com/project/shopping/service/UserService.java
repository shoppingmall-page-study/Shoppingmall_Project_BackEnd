package com.project.shopping.service;


import com.project.shopping.Error.CustomException;
import com.project.shopping.Error.ErrorCode;
import com.project.shopping.dto.UserDTO;
import com.project.shopping.dto.requestDTO.UserRequestDTO.UserDeleteRequestDTO;
import com.project.shopping.dto.requestDTO.UserRequestDTO.UserOAuthAddInfoRequestDTO;
import com.project.shopping.dto.requestDTO.UserRequestDTO.UserUpdateRequestDTO;
import com.project.shopping.dto.responseDTO.UserResponseDTO.*;
import com.project.shopping.dto.requestDTO.UserRequestDTO.UserJoinRequestDTO;
import com.project.shopping.dto.responseDTO.UserResponseDTO.UserDeleteResponseDTO;
import com.project.shopping.dto.responseDTO.UserResponseDTO.UserInfoResponseDTO;
import com.project.shopping.dto.responseDTO.UserResponseDTO.UserJoinResponseDTO;
import com.project.shopping.dto.responseDTO.UserResponseDTO.UserUpdateResponseDTO;
import com.project.shopping.model.User;
import com.project.shopping.repository.UserRepository;
import com.project.shopping.security.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final  UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final EmailAuthenticationService emailAuthenticationService;

    public UserJoinResponseDTO create(UserJoinRequestDTO userJoinRequestDTO){

        //이메일 중복 체크
        if(userRepository.existsByEmail(userJoinRequestDTO.getEmail())){
            throw new CustomException(ErrorCode.DuplicatedEmilException);
        }

        if(!emailAuthenticationService.isEmailAuthenticated(userJoinRequestDTO.getEmail())){
            throw new CustomException(ErrorCode.UnauthorizedEmailException);
        }
        // 닉네임 중복 체크
        if(userRepository.existsByNickname(userJoinRequestDTO.getNickname())){
            throw  new CustomException(ErrorCode.DuplicatedNickNameException);
        }

        User user = User.builder()
                .email(userJoinRequestDTO.getEmail())
                .password(passwordEncoder.encode((userJoinRequestDTO.getPassword())))
                .username(userJoinRequestDTO.getUsername())
                .address(userJoinRequestDTO.getAddress()).age(userJoinRequestDTO.getAge())
                .roles(Role.ROLE_USER)
                .nickname(userJoinRequestDTO.getNickname()).phoneNumber(userJoinRequestDTO.getPhoneNumber())
                .status("active")
                .postCode(userJoinRequestDTO.getPostCode()).build();


        user = userRepository.save(user);
        UserJoinResponseDTO userJoinResponseDTO = UserJoinResponseDTO.builder()
                .email(user.getEmail())
                .username(user.getUsername())
                .address(user.getAddress())
                .postCode(user.getPostCode())
                .age(user.getAge())
                .nickname(user.getNickname())
                .phoneNumber(user.getPhoneNumber())
                .createDate(user.getCreateDate())
                .modifiedDate(user.getModifiedDate())
                .build();


        return userJoinResponseDTO;
    }

    public  User oAuthLoginCreateUser(String email, String name){
        User user = User.builder()
                .email(email)
                .password(generateRandomPassword())
                .username(name)
                .address("null").age(1000)
                .roles(Role.ROLE_GUEST)
                .status("null")
                .postCode("null")
                .nickname("null").phoneNumber("null").build();
        return userRepository.save(user);
    }


    public UserOAuthAddInfoResponseDTO oauthUserInfoAdd(String email, UserOAuthAddInfoRequestDTO userOAuthAddInfoRequestDTO){
        User user = userRepository.findByEmail(email)
                .orElseThrow(()->new CustomException(ErrorCode.NotFoundUserException));

        if(userRepository.existsByNickname(userOAuthAddInfoRequestDTO.getNickname())){
            throw  new CustomException(ErrorCode.DuplicatedNickNameException);
        }


        user.setAddress(userOAuthAddInfoRequestDTO.getAddress());
        user.setPostCode(userOAuthAddInfoRequestDTO.getPostCode());
        user.setAge(userOAuthAddInfoRequestDTO.getAge());
        user.setNickname(userOAuthAddInfoRequestDTO.getNickname());
        user.setRoles(Role.ROLE_USER);
        user.setPhoneNumber(userOAuthAddInfoRequestDTO.getPhoneNumber());

        userRepository.save(user);

        UserOAuthAddInfoResponseDTO userOAuthAddInfoResponseDTO = UserOAuthAddInfoResponseDTO.builder()
                .email(user.getEmail())
                .username(user.getUsername())
                .address(user.getAddress())
                .postCode(user.getPostCode())
                .age(user.getAge())
                .nickname(user.getNickname())
                .phoneNumber(user.getPhoneNumber())
                .createDate(user.getCreateDate())
                .modifiedDate(user.getModifiedDate())
                .build();

        return userOAuthAddInfoResponseDTO;
    }
    public UserInfoResponseDTO findUserByEmail(String email){

        User user = userRepository.findByEmail(email)
                .orElseThrow(()->new CustomException(ErrorCode.NotFoundUserException));

        UserInfoResponseDTO userInfoResponseDTO = UserInfoResponseDTO.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .age(user.getAge())
                .address(user.getAddress())
                .nickname(user.getNickname())
                .phoneNumber(user.getPhoneNumber())
                .postCode(user.getPostCode())
                .createDate(user.getCreateDate())
                .modifiedDate(user.getModifiedDate())
                .build();

        return userInfoResponseDTO;
    }

    //login 인증
//    public User getByCredentials(final String email , final String password, final PasswordEncoder encoder){
//        final User original = userRepository.findByEmail(email);
//        if(original != null && encoder.matches(password, original.getPassword())){
//            return original;
//        }
//        return null;
//    }
    public UserDeleteResponseDTO delete(UserDeleteRequestDTO userDeleteRequestDTO, User user){

        userRepository.findByEmail(user.getEmail())
                .orElseThrow(()->new CustomException(ErrorCode.NotFoundUserException));

        if(passwordEncoder.matches(userDeleteRequestDTO.getPassword(), user.getPassword() )){

            user.setStatus("Disable");
            userRepository.save(user);
        }else{
            throw  new CustomException(ErrorCode.BadPasswordException);
        }
        UserDeleteResponseDTO userDeleteResponseDTO = UserDeleteResponseDTO.
                builder().
                email(user.getEmail())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .build();
        return  userDeleteResponseDTO;

    }

    public Boolean existsByEmail(String email){

        return userRepository.existsByEmail(email);
    }

    public Boolean existsByNickname(String nickname){
        return userRepository.existsByNickname(nickname);}





    public UserUpdateResponseDTO updateUser(UserUpdateRequestDTO userUpdateRequestDTO, User user){

        userRepository.findByEmail(user.getEmail())
            .orElseThrow(()->new CustomException(ErrorCode.NotFoundUserException));// 해당 user찾기

        if(!user.getEmail().equals(userUpdateRequestDTO.getEmail())){
                throw  new CustomException(ErrorCode.BadParameterException);
        }

        if(userRepository.existsByNickname(userUpdateRequestDTO.getNickname())){
            throw  new CustomException(ErrorCode.DuplicatedNickNameException);
        }


        user.setUsername(userUpdateRequestDTO.getUsername());
        user.setAddress(userUpdateRequestDTO.getAddress());
        user.setAge(userUpdateRequestDTO.getAge());
        user.setPostCode(userUpdateRequestDTO.getPostCode());
        user.setNickname(userUpdateRequestDTO.getNickname());
        userRepository.save(user);

        UserUpdateResponseDTO userUpdateResponseDTO = UserUpdateResponseDTO.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .age(user.getAge())
                .address(user.getAddress())
                .nickname(user.getNickname())
                .phoneNumber(user.getPhoneNumber())
                .postCode(user.getPostCode())
                .createDate(user.getCreateDate())
                .modifiedDate(user.getModifiedDate())
                .build();

        return  userUpdateResponseDTO;
    }

    // 랜덤 문자열 password

    private String generateRandomPassword(){
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();
        String generatedRandomString = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        return passwordEncoder.encode(generatedRandomString);

    }


    public User findByEmail(String email){
        return  userRepository.findByEmail(email)
                .orElseThrow(()-> new CustomException(ErrorCode.NotFoundUserException));// 유저 찾기
    }





}
