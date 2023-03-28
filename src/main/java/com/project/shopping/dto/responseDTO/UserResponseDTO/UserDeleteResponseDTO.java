package com.project.shopping.dto.responseDTO.UserResponseDTO;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserDeleteResponseDTO {
    private  String email;
    private  String username;
    private  String nickname;


}
