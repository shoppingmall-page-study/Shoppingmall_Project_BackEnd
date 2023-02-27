package com.project.shopping.dto.responseDTO.EmailAuthenticationResponseDTO;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class CheckAuthCodeResponseDTO {
    private String email;

    @Builder
    public CheckAuthCodeResponseDTO(String email) {
        this.email = email;
    }
}
