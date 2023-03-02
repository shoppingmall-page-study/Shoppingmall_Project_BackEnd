package com.project.shopping.service;


import com.project.shopping.Error.CustomException;
import com.project.shopping.Error.ErrorCode;
import com.project.shopping.auth.PrincipalDetails;
import com.project.shopping.dto.UserDTO;
import com.project.shopping.dto.requestDTO.UserRequestDTO.UserDeleteRequestDTO;
import com.project.shopping.dto.requestDTO.UserRequestDTO.UserUpdateRequestDTO;
import com.project.shopping.dto.requestDTO.UserRequestDTO.userJoinRequestDTO;
import com.project.shopping.dto.responseDTO.UserResponseDTO.UserDeleteResponseDTO;
import com.project.shopping.dto.responseDTO.UserResponseDTO.UserInfoResponseDTO;
import com.project.shopping.dto.responseDTO.UserResponseDTO.UserJoinResponseDTO;
import com.project.shopping.dto.responseDTO.UserResponseDTO.UserUpdateResponseDTO;
import com.project.shopping.model.User;
import com.project.shopping.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final  UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final EmailAuthenticationService emailAuthenticationService;

    public UserJoinResponseDTO create(userJoinRequestDTO userJoinRequestDTO){

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
                .roles("ROLE_USER")
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

    public UserDTO saveUser(UserDTO userDTO, Authentication authentication){


        PrincipalDetails userDetails = (PrincipalDetails) authentication.getPrincipal();
        String email = userDetails.getUser().getEmail();
        User user = userRepository.findByEmail(email)
                        .orElseThrow(()->new CustomException(ErrorCode.NotFoundUserException));
        user.setAddress(userDTO.getAddress());
        user.setAge(userDTO.getAge());
        user.setPostCode(userDTO.getPostCode());
        user.setNickname(userDTO.getNickname());
        user.setPhoneNumber(userDTO.getPhoneNumber());

        userRepository.save(user);
        UserDTO response = UserDTO.builder().username(user.getUsername()).email(user.getEmail())
                .age(user.getAge()).address(user.getAddress())
                .nickname(user.getNickname()).phoneNumber(user.getPhoneNumber()).build();
        return  response;
    }

    public UserInfoResponseDTO findEmailByUser(Authentication authentication){

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        String email = principalDetails.getUser().getEmail();

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
    public UserDeleteResponseDTO delete(UserDeleteRequestDTO userDeleteRequestDTO, Authentication authentication){
        PrincipalDetails userDetails = (PrincipalDetails) authentication.getPrincipal();
        String email = userDetails.getUser().getEmail();
        User user = userRepository.findByEmail(email)
                .orElseThrow(()->new CustomException(ErrorCode.NotFoundUserException));


        if(passwordEncoder.matches(userDeleteRequestDTO.getPassword(), userDetails.getPassword() )){

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
        if(userRepository.existsByEmail(email)){
            throw  new CustomException(ErrorCode.DuplicatedEmilException);
        }
        return userRepository.existsByEmail(email);
    }

    public Boolean existsByNickname(String nickname){
        if(userRepository.existsByNickname(nickname)){
            throw  new CustomException(ErrorCode.DuplicatedNickNameException);
        }
        return userRepository.existsByNickname(nickname);}





    public UserUpdateResponseDTO updateUser(Authentication authentication, UserUpdateRequestDTO userUpdateRequestDTO){
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        String email = principalDetails.getUser().getEmail();
        User user = userRepository.findByEmail(email)
                .orElseThrow(()->new CustomException(ErrorCode.NotFoundUserException));// 해당 user찾기

        if(!user.getNickname().equals(userUpdateRequestDTO.getNickname())){
            // 만약 현제 유저 닉네임이  들어온 유저 닉네임과 동일하지 않다면
            if(userRepository.existsByNickname(userUpdateRequestDTO.getNickname())){
                // 들어온 값이 db에 있을시 에러
                throw  new CustomException(ErrorCode.DuplicatedNickNameException);
            }
            user.setNickname(userUpdateRequestDTO.getNickname());
        }

        if(!user.getEmail().equals(userUpdateRequestDTO.getEmail())){
            if(userRepository.existsByEmail(userUpdateRequestDTO.getEmail())){
                throw  new CustomException(ErrorCode.DuplicatedEmilException);
            }
        }
        user.setUsername(userUpdateRequestDTO.getUsername());
        user.setAddress(userUpdateRequestDTO.getAddress());
        user.setAge(userUpdateRequestDTO.getAge());
        user.setPostCode(userUpdateRequestDTO.getPostCode());
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


}
