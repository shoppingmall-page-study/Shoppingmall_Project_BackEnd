package com.project.shopping.dto.requestDTO.CartRequestDTO;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@NoArgsConstructor
@Getter
public class CartCreateRequestDTO {
    private  long productNum;

    @Builder
    public CartCreateRequestDTO(long productNum) {
        this.productNum = productNum;
    }

}
