package com.project.shopping.dto.requestDTO.UserRequestDTO;

import com.project.shopping.model.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@NoArgsConstructor
@Getter
public class UserUpdateRequestDTO {
    @NotBlank
    private  String username;
    @NotBlank
    private  String address;
    @NotNull
    private int age;
    @NotBlank
    private String nickname;


    @NotBlank
    @Pattern(regexp = "^01(?:0|1|[6-9])[.-]?(\\d{3}|\\d{4})[.-]?(\\d{4})$")
    private  String phoneNumber;

    @NotBlank
    private String postCode;

    public User toEntity(){
        return User.builder()
                .username(this.username)
                .address(this.address)
                .postCode(this.postCode)
                .age(this.age)
                .nickname(this.nickname)
                .phoneNumber(this.phoneNumber)
                .build();
    }


}
