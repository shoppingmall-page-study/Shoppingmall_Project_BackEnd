package com.project.shopping.dto.responseDTO.EmailAuthenticationResponseDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class SendAuthCodeResponseDTO {

    private String email;

    private SendAuthCodeResponseDTO(String email) {
        this.email = email;
    }

    public static SendAuthCodeResponseDTO toSendAuthCodeResponseDTO(String email){
        return new SendAuthCodeResponseDTO(email);
    }

}
