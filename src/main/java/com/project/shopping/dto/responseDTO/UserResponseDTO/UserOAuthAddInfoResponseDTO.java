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


}
