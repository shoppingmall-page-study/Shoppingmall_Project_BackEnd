package com.project.shopping.dto.responseDTO.UserResponseDTO;

import com.project.shopping.model.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserDeleteResponseDTO {
    private  String email;
    private  String username;
    private  String nickname;


    @Builder
    private UserDeleteResponseDTO(String email, String username, String nickname) {
        this.email = email;
        this.username = username;
        this.nickname = nickname;
    }

    public static UserDeleteResponseDTO toUserDeleteResponseDTO(User user){
        return UserDeleteResponseDTO.builder()
                .email(user.getEmail())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .build();
    }
}
