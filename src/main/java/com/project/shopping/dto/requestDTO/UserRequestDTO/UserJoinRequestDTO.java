package com.project.shopping.dto.requestDTO.UserRequestDTO;


import com.project.shopping.model.User;
import com.project.shopping.security.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;


@NoArgsConstructor
@Getter
public class UserJoinRequestDTO {

    @Email
    @NotBlank
    private String email;
    @NotBlank
    private String password;

    @NotBlank
    private  String username;

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


    public User toEntity(String passwordEncode, Role role, String status){
        return User.builder()
                .email(this.email)
                .password(passwordEncode)
                .username(this.username)
                .address(this.address)
                .postCode(this.postCode)
                .age(this.age)
                .nickname(this.nickname)
                .phoneNumber(this.phoneNumber)
                .roles(role)
                .status(status)
                .build();
    }
}
