package com.project.shopping.dto.requestDTO.CartRequestDTO;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class CartUpdateRequestDTO {
    private  long productNum;

    @Builder
    public CartUpdateRequestDTO(long productNum) {
        this.productNum = productNum;
    }
}
