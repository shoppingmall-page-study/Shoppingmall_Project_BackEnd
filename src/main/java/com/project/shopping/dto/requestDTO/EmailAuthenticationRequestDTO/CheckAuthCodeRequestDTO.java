package com.project.shopping.dto.requestDTO.EmailAuthenticationRequestDTO;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@Getter
public class CheckAuthCodeRequestDTO {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String authCode;

    @Builder
    public CheckAuthCodeRequestDTO(String email, String authCode) {
        this.email = email;
        this.authCode = authCode;
    }
}
