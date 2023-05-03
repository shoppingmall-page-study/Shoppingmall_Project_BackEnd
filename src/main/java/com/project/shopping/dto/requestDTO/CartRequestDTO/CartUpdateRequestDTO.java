package com.project.shopping.dto.requestDTO.CartRequestDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
public class CartUpdateRequestDTO {

    @NotNull
    private  long productNum;
}
