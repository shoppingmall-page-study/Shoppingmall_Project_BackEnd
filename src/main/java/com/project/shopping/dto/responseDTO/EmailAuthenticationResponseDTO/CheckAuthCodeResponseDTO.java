package com.project.shopping.dto.responseDTO.EmailAuthenticationResponseDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class CheckAuthCodeResponseDTO {
    private String email;

    private CheckAuthCodeResponseDTO(String email) {
        this.email = email;
    }

    public static CheckAuthCodeResponseDTO toCheckAuthCodeResponseDTO(String email) {
        return new CheckAuthCodeResponseDTO(email);
    }
}
