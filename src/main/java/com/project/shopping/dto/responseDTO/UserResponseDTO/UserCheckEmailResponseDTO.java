package com.project.shopping.dto.responseDTO.UserResponseDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserCheckEmailResponseDTO {
    private  String email;

    private UserCheckEmailResponseDTO(String email) {
        this.email = email;
    }

    public static UserCheckEmailResponseDTO toUserCheckEmailResponseDTO(String email){
        return new UserCheckEmailResponseDTO(email);
    }

}
