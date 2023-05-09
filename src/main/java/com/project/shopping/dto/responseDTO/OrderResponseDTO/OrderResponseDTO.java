package com.project.shopping.dto.responseDTO.OrderResponseDTO;

import com.project.shopping.model.Order;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;

@NoArgsConstructor
@Getter
public class OrderResponseDTO {
    private String orderId;
    private ArrayList<ProductInOrderResponseDTO> products;
    private String orderComplete;
    private long amount;
    private LocalDateTime orderTime;

    @Builder
    private OrderResponseDTO(String orderId, ArrayList<ProductInOrderResponseDTO> products, String orderComplete, long amount, LocalDateTime orderTime) {
        this.orderId = orderId;
        this.products = products;
        this.orderComplete = orderComplete;
        this.amount = amount;
        this.orderTime = orderTime;
    }

    public static OrderResponseDTO toOrderResponseDTO(Order order,ArrayList<ProductInOrderResponseDTO> productsResponseDTO){
        return OrderResponseDTO.builder()
                .orderId(order.getId())
                .products(productsResponseDTO)
                .orderComplete(order.getOrderComplete())
                .amount(order.getAmount())
                .orderTime(order.getOrderTime())
                .build();
    }

}
