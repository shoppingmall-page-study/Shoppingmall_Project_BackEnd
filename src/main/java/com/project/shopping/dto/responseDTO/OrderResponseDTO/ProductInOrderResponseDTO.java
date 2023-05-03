package com.project.shopping.dto.responseDTO.OrderResponseDTO;

import com.project.shopping.model.Product;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ProductInOrderResponseDTO {

    private int productId;
    private String productName;
    private String imgUrl;
    private int productNum;

    @Builder
    private ProductInOrderResponseDTO(int productId, String productName, String imgUrl, int productNum) {
        this.productId = productId;
        this.productName = productName;
        this.imgUrl = imgUrl;
        this.productNum = productNum;
    }

    public static ProductInOrderResponseDTO toProductInOrderResponseDTO(Product product, int productNum){
        return ProductInOrderResponseDTO.builder()
                .productId(product.getId())
                .productName(product.getName())
                .imgUrl(product.getImgUrl())
                .productNum(productNum)
                .build();
    }
}
