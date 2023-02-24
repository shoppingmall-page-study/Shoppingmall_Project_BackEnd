package com.project.shopping.dto.responseDTO.EmailAuthenticationResponseDTO;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class SendAuthCodeResponseDTO {

    private String email;

    @Builder
    public SendAuthCodeResponseDTO(String email) {
        this.email = email;
    }
}
