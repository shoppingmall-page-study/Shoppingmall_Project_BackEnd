package com.project.shopping.dto.responseDTO.OrderResponseDTO;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ProductInOrderResponseDTO {

    private int productId;
    private String productName;
    private int productNum;

    @Builder
    public ProductInOrderResponseDTO(int productId, String productName, int productNum) {
        this.productId = productId;
        this.productName = productName;
        this.productNum = productNum;
    }
}
