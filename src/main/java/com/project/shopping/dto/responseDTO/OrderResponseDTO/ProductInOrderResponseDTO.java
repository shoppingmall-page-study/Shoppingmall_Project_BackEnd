package com.project.shopping.dto.responseDTO.OrderResponseDTO;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ProductInOrderResponseDTO {

    private int product;
    private String productName;
    private String imgUrl;
    private int productNum;

    @Builder
    public ProductInOrderResponseDTO(int product, String productName, String imgUrl, int productNum) {
        this.product = product;
        this.productName = productName;
        this.imgUrl = imgUrl;
        this.productNum = productNum;
    }
}
