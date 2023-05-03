package com.project.shopping.dto.responseDTO.CartResponseDTO;

import com.project.shopping.model.Cart;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@NoArgsConstructor
@Getter
public class CartDeleteResponseDTO {
    private  int cartId;
    private ProductResponseDTO product;
    private  long productNum;
    private LocalDateTime createDate;

    @Builder
    private CartDeleteResponseDTO(int cartId, ProductResponseDTO product, long productNum, LocalDateTime createDate) {
        this.cartId = cartId;
        this.product = product;
        this.productNum = productNum;
        this.createDate = createDate;

    }

    public static CartDeleteResponseDTO toCartDeleteResponseDTO(Cart cart, ProductResponseDTO productResponseDTO){
        return CartDeleteResponseDTO.builder()
                .cartId(cart.getId())
                .product(productResponseDTO)
                .productNum(cart.getProductNum())
                .createDate(cart.getCreateDate())
                .build();
    }
}
