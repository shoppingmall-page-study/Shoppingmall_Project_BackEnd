package com.project.shopping.dto.requestDTO.CartRequestDTO;

import com.project.shopping.model.Cart;
import com.project.shopping.model.Product;
import com.project.shopping.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
public class CartCreateRequestDTO {

    @NotNull
    private  long productNum;

    public Cart toEntity(Product product, User user){
        return Cart.builder()
                .product(product)
                .user(user)
                .productNum(this.productNum)
                .status("active")
                .build();
    }

}
