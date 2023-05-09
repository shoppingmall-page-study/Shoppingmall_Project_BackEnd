package com.project.shopping.dto.responseDTO.PaymentResponseDTO;

import com.project.shopping.dto.responseDTO.OrderResponseDTO.ProductInOrderResponseDTO;
import com.project.shopping.model.Order;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;

@NoArgsConstructor
@Getter
public class PaymentCompleteResponseDTO {
    private String orderId;
    @Setter
    private  String orderComplete;
    private  ArrayList<ProductInOrderResponseDTO> products;
    private  Long amount;
    @Setter
    private LocalDateTime paymentCompleteDate;

    @Builder
    private PaymentCompleteResponseDTO(String orderId, String orderComplete, ArrayList<ProductInOrderResponseDTO> products, Long amount, LocalDateTime paymentCompleteDate) {
        this.orderId = orderId;
        this.orderComplete = orderComplete;
        this.products = products;
        this.amount = amount;
        this.paymentCompleteDate = paymentCompleteDate;
    }

    public static PaymentCompleteResponseDTO toPaymentCompleteResponseDTO(Order order, ArrayList<ProductInOrderResponseDTO> productInOrderResponseDTOList){
        return PaymentCompleteResponseDTO.builder()
                .orderId(order.getId())
                .orderComplete(order.getOrderComplete())
                .products(productInOrderResponseDTOList)
                .amount(order.getAmount())
                .paymentCompleteDate(order.getPaymentCompleteDate())
                .build();
    }
}