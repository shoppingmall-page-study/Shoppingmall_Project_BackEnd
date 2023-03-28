package com.project.shopping.dto.responseDTO.UserResponseDTO;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class UserUpdateResponseDTO {
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
