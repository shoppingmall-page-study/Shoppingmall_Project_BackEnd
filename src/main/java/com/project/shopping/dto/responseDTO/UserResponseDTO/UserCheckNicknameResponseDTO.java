package com.project.shopping.dto.responseDTO.UserResponseDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserCheckNicknameResponseDTO {
    private  String nickname;

    private UserCheckNicknameResponseDTO(String nickname) {
        this.nickname = nickname;
    }

    public static UserCheckNicknameResponseDTO createUserCheckNicknameResponseDTO(String nickname) {
        return new UserCheckNicknameResponseDTO(nickname);
    }
}
