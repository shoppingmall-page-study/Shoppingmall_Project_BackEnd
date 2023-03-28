package com.project.shopping.service;


import com.project.shopping.Error.CustomException;
import com.project.shopping.Error.ErrorCode;
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
import org.modelmapper.ModelMapper;
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

        // dto -> entity
        User user = userJoinRequestDTO.toEntity(passwordEncoder.encode(userJoinRequestDTO.getPassword()),Role.ROLE_USER,"active");
        // user 저장 후
        userRepository.save(user);

        // entity -> dto 변환 후 return
        return UserJoinResponseDTO.toUserJoinResponseDTO(user);

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


    public UserOAuthAddInfoResponseDTO oauthUserInfoAdd(User user, UserOAuthAddInfoRequestDTO userOAuthAddInfoRequestDTO){

        if(userRepository.existsByNickname(userOAuthAddInfoRequestDTO.getNickname())){
            throw  new CustomException(ErrorCode.DuplicatedNickNameException);
        }

        user.oauthInfoAdd(userOAuthAddInfoRequestDTO.toEntity(Role.ROLE_USER));
        userRepository.save(user);
        log.info(user.getRoles()+", user 권한");

        return UserOAuthAddInfoResponseDTO.toUserOAuthAddInfoResponseDTO(user);

    }

    public UserInfoResponseDTO findUserByEmail(String email){

        User user = userRepository.findByEmail(email)
                .orElseThrow(()->new CustomException(ErrorCode.NotFoundUserException));

        return UserInfoResponseDTO.toUserInfoResponseDTO(user);
    }


    public UserDeleteResponseDTO delete(UserDeleteRequestDTO userDeleteRequestDTO, User user){

        userRepository.findByEmail(user.getEmail())
                .orElseThrow(()->new CustomException(ErrorCode.NotFoundUserException));

        if(passwordEncoder.matches(userDeleteRequestDTO.getPassword(), user.getPassword() )){

            user.delete();
            userRepository.save(user);
        }else{
            throw  new CustomException(ErrorCode.BadPasswordException);
        }
        return  UserDeleteResponseDTO.toUserDeleteResponseDTO(user);

    }

    public Boolean existsByEmail(String email){

        return userRepository.existsByEmail(email);
    }

    public Boolean existsByNickname(String nickname){
        return userRepository.existsByNickname(nickname);
    }





    public UserUpdateResponseDTO updateUser(UserUpdateRequestDTO userUpdateRequestDTO, User user){


        if(userRepository.existsByNickname(userUpdateRequestDTO.getNickname())){
            throw  new CustomException(ErrorCode.DuplicatedNickNameException);
        }




        return UserUpdateResponseDTO.toUserUpdateResponseDTO(user);
    }

    public User findByEmail(String email){
        return  userRepository.findByEmail(email)
                .orElseThrow(()-> new CustomException(ErrorCode.NotFoundUserException));// 유저 찾기
    }

    // 닉네임 체크 api
    public UserCheckNicknameResponseDTO checkNickname(String value){
        if(userRepository.existsByNickname(value)){
            new CustomException(ErrorCode.DuplicatedNickNameException);
        }
        UserCheckNicknameResponseDTO userCheckNicknameResponseDTO =  UserCheckNicknameResponseDTO.UserCheckNicknameResponseDTO(value);

        return  userCheckNicknameResponseDTO;
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


}
