package com.project.shopping.dto.requestDTO.UserRequestDTO;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;



@NoArgsConstructor
@Getter
public class userJoinRequestDTO {
    private String email;
    private String password;
    private  String username;
    private  String address;
    private String postCode;
    private int age;
    private  String nickname;
    private  String phoneNumber;


    @Builder

    public userJoinRequestDTO(String email, String password, String username, String address, String postCode, int age, String nickname, String phoneNumber) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.address = address;
        this.postCode = postCode;
        this.age = age;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
    }
}
