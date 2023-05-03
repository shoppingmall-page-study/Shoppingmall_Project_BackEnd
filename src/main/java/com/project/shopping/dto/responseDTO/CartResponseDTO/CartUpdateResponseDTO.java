package com.project.shopping.dto.responseDTO.CartResponseDTO;

import com.project.shopping.model.Cart;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@NoArgsConstructor
@Getter
public class CartUpdateResponseDTO {
    private  int cartId;
    private ProductResponseDTO product;
    private  long productNum;
    private LocalDateTime createDate;


    @Builder
    private CartUpdateResponseDTO(int cartId, ProductResponseDTO product, long productNum, LocalDateTime createDate, LocalDateTime modifiedDate) {
        this.cartId = cartId;
        this.product = product;
        this.productNum = productNum;
        this.createDate = createDate;

    }

    public static CartUpdateResponseDTO toCartUpdateResponseDTO(Cart cart, ProductResponseDTO productResponseDTO){
        return CartUpdateResponseDTO.builder()
                .cartId(cart.getId())
                .product(productResponseDTO)
                .productNum(cart.getProductNum())
                .createDate(cart.getCreateDate())
                .build();
    }

}
