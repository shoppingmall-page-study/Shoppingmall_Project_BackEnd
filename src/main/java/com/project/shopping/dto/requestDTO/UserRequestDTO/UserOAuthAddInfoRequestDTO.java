package com.project.shopping.dto.requestDTO.UserRequestDTO;

import com.project.shopping.model.User;
import com.project.shopping.security.Role;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

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


    public User toEntity(Role role){
        System.out.println(role);

        return User.builder()
                .address(this.address)
                .postCode(this.postCode)
                .age(this.age)
                .roles(role)
                .nickname(this.nickname)
                .phoneNumber(this.phoneNumber)
                .build();
    }

}
