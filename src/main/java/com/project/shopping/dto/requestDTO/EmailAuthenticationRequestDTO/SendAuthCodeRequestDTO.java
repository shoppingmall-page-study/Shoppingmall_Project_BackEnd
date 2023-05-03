package com.project.shopping.dto.requestDTO.EmailAuthenticationRequestDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@Getter
public class SendAuthCodeRequestDTO {

    @NotBlank
    @Email
    private String email;
}
