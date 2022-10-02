package com.project.shopping.dto.requestDTO.UserRequestDTO;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserDeleteRequestDTO {
    private  String password;

    @Builder
    public UserDeleteRequestDTO(String password) {
        this.password = password;
    }
}
