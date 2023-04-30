package com.project.shopping.dto.responseDTO.UserResponseDTO;


import com.project.shopping.model.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class UserJoinResponseDTO {
    private String email;
    private  String username;
    private  String address;
    private String postCode;
    private int age;
    private  String nickname;
    private  String phoneNumber;
    private LocalDateTime createDate;
    private  LocalDateTime modifiedDate;

    @Builder
    private UserJoinResponseDTO(String email, String username, String address, String postCode, int age, String nickname, String phoneNumber, LocalDateTime createDate, LocalDateTime modifiedDate) {
        this.email = email;
        this.username = username;
        this.address = address;
        this.postCode = postCode;
        this.age = age;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.createDate = createDate;
        this.modifiedDate = modifiedDate;
    }
    public static  UserJoinResponseDTO toUserJoinResponseDTO(User user){
        return UserJoinResponseDTO.builder()
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

    }

}
