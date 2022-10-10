package com.project.shopping.dto.responseDTO.OrderResponseDTO;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;

@NoArgsConstructor
@Getter
public class OrderResponseDTO {
    private int orderId;
    private ArrayList<ProductInOrderResponseDTO> products;
    private String orderComplete;
    private long amount;
    private LocalDateTime orderTime;

    @Builder
    public OrderResponseDTO(int orderId, ArrayList<ProductInOrderResponseDTO> products, String orderComplete, long amount, LocalDateTime orderTime) {
        this.orderId = orderId;
        this.products = products;
        this.orderComplete = orderComplete;
        this.amount = amount;
        this.orderTime = orderTime;
    }
}
