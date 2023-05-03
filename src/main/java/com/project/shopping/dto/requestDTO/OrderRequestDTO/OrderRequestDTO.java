package com.project.shopping.dto.requestDTO.OrderRequestDTO;

import com.project.shopping.model.Order;
import com.project.shopping.model.OrderDetail;
import com.project.shopping.model.Product;
import com.project.shopping.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;

@NoArgsConstructor
@Getter
public class OrderRequestDTO {

    @NotNull
    @Size(min = 1)
    private ArrayList<Integer> productsId;

    @NotNull
    @Size(min = 1)
    private ArrayList<Integer> productsNumber;

    public Order toEntity(User user, ArrayList<OrderDetail> orderDetails, long amount){

        return Order.builder()
                .user(user)
                .products(orderDetails)
                .orderComplete("ready")
                .status("active")
                .amount(amount)
                .build();
    }

    public OrderDetail toOrderDetailEntity(Product product, int productIndex, Order order){
        return new OrderDetail(product, productsNumber.get(productIndex), order);
    }

}



