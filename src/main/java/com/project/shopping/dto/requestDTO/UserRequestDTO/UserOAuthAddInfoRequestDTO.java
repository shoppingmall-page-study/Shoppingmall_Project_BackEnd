package com.project.shopping.dto.requestDTO.UserRequestDTO;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@RequiredArgsConstructor
public class UserOAuthAddInfoRequestDTO {


    @NotNull
    private  String address;

    @NotBlank
    private String postCode;
    @NotNull
    private int age;
    @NotBlank
    private  String nickname;
    @NotBlank
    @Pattern(regexp = "^01(?:0|1|[6-9])[.-]?(\\d{3}|\\d{4})[.-]?(\\d{4})$")
    private  String phoneNumber;

    @Builder

    public UserOAuthAddInfoRequestDTO(String address, String postCode, int age, String nickname, String phoneNumber) {
        this.address = address;
        this.postCode = postCode;
        this.age = age;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
    }
}
