package com.project.shopping.dto.responseDTO.UserResponseDTO;

import com.project.shopping.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@AllArgsConstructor
public class UserDeleteResponseDTO {
    private  String email;
    private  String username;
    private  String nickname;



    public static UserDeleteResponseDTO toUserDeleteResponseDTO(User user){
        return  new UserDeleteResponseDTO(user.getEmail(), user.getUsername(),user.getNickname());
    }
}
