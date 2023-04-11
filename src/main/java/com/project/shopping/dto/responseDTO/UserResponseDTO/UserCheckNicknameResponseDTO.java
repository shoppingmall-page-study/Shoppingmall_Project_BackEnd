package com.project.shopping.dto.responseDTO.UserResponseDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@AllArgsConstructor
public class UserCheckNicknameResponseDTO {
    private  String nickname;

    public static UserCheckNicknameResponseDTO UserCheckNicknameResponseDTO(String nickname) {
        return new UserCheckNicknameResponseDTO(nickname);
    }
}
