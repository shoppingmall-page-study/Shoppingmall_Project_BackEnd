package com.project.shopping.dto.requestDTO.UserRequestDTO;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserUpdateRequestDTO {
    private  String username;
    private  String address;
    private int age;
    private String nickname;
    private  String phoneNumber;
    private String email;
    private String postCode;


    @Builder
    public UserUpdateRequestDTO(String username, String address, int age, String nickname, String phoneNumber, String email, String postCode) {
        this.username = username;
        this.address = address;
        this.age = age;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.postCode = postCode;
    }
}
