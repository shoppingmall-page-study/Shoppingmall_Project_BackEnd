package com.project.shopping.service;

import com.project.shopping.Error.CustomException;
import com.project.shopping.Error.ErrorCode;
import com.project.shopping.dto.requestDTO.OrderRequestDTO.OrderRequestDTO;
import com.project.shopping.dto.responseDTO.OrderResponseDTO.OrderResponseDTO;
import com.project.shopping.dto.responseDTO.OrderResponseDTO.ProductInOrderResponseDTO;
import com.project.shopping.model.*;
import com.project.shopping.repository.OrderDetailRepository;
import com.project.shopping.repository.OrderRepository;
import com.project.shopping.repository.ProductRepository;
import com.project.shopping.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderDetailRepository orderDetailRepository;

    public OrderResponseDTO create(User user, OrderRequestDTO orderRequestDTO)throws CustomException {

        ArrayList<ProductInOrderResponseDTO>  responseProductsDTO = new ArrayList<>();
        ArrayList<OrderDetail> products = new ArrayList<>();

        long totalAmount = 0;
        Order order = Order.builder()
                .user(user)
                .orderComplete("ready")
                .status("active")
                .products(products)
                .build();

        for(int i = 0; i < orderRequestDTO.getProductsId().size(); i++){
            Product product = productRepository.findById((int)orderRequestDTO.getProductsId().get(i))
                    .orElseThrow(()-> new CustomException(ErrorCode.NotFoundProductException));
            int productNum = orderRequestDTO.getProductsNumber().get(i);

            OrderDetail orderDetail = OrderDetail.builder()
                    .order(order)
                    .product(product)
                    .productNum(productNum)
                    .build();

            order.getProducts().add(orderDetail);

            ProductInOrderResponseDTO productDTO = ProductInOrderResponseDTO.builder()
                    .product(product.getId())
                    .productName(product.getTitle())
                    .imgUrl(product.getImgUrl())
                    .productNum(productNum)
                    .build();

            responseProductsDTO.add(productDTO);

            totalAmount += product.getPrice() * productNum;
        }

        order.setAmount(totalAmount);

        Order savedOrder = orderRepository.save(order);

        OrderResponseDTO orderResponseDTO = OrderResponseDTO.builder()
                .orderId(savedOrder.getId())
                .products(responseProductsDTO)
                .orderComplete(savedOrder.getOrderComplete())
                .amount(savedOrder.getAmount())
                .orderTime(savedOrder.getOrderTime())
                .build();

        return orderResponseDTO;
    }

    public Order update(Order order){
        if(order.getProducts()== null){
            throw new CustomException(ErrorCode.NotFoundProductException);
        }
        return orderRepository.save(order);
    }

    public List<OrderResponseDTO> getOrderList(User user){

        List<Order> orderList = orderRepository.findAllByUser(user);
        List<OrderResponseDTO> orders = new ArrayList<>();

        for(Order order : orderList){
            ArrayList<ProductInOrderResponseDTO> products = new ArrayList<>();

            for(int i = 0; i < order.getProducts().size(); i++){
                ProductInOrderResponseDTO productDTO;
                Product product = order.getProducts().get(i).getProduct();
                int productNum = order.getProducts().get(i).getProductNum();

                productDTO = ProductInOrderResponseDTO.builder()
                        .product(product.getId())
                        .productName(product.getName())
                        .productNum(productNum)
                        .imgUrl(product.getImgUrl())
                        .build();

                products.add(productDTO);
            }

            OrderResponseDTO orderDTO = OrderResponseDTO.builder()
                    .orderId(order.getId())
                    .products(products)
                    .orderComplete(order.getOrderComplete())
                    .amount(order.getAmount())
                    .orderTime(order.getOrderTime())
                    .build();

            orders.add(orderDTO);
        }

        return orders;
    }
}
