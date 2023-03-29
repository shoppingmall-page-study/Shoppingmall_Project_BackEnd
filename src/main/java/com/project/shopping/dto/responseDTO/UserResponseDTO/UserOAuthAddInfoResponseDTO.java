package com.project.shopping.dto.responseDTO.UserResponseDTO;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class UserOAuthAddInfoResponseDTO {
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

    public UserOAuthAddInfoResponseDTO(String email, String username, String address, String postCode, int age, String nickname, String phoneNumber, LocalDateTime createDate, LocalDateTime modifiedDate) {
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
}
