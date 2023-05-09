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
    private final OrderDetailRepository orderDetailRepository;

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderResponseDTO create(User user, OrderRequestDTO orderRequestDTO)throws CustomException {

        ArrayList<OrderDetail> orderDetails = new ArrayList<>();
        ArrayList<ProductInOrderResponseDTO> productsResponseDTO = new ArrayList<>();
        List<Product> products = productRepository.findAllById(orderRequestDTO.getProductsId());

        long amount  = 0;

        for(Product p : products)
            amount += p.getPrice();

        Order order = orderRequestDTO.toEntity(user, orderDetails, amount);

        if(products.size() != orderRequestDTO.getProductsId().size())
            throw new CustomException(ErrorCode.NotFoundProductException);

        for(int i = 0; i < products.size(); i++){
            OrderDetail orderDetail = orderRequestDTO.toOrderDetailEntity(products.get(i), i, order);
            orderDetails.add(orderDetail);
            orderDetailRepository.save(orderDetail);

            productsResponseDTO.add(ProductInOrderResponseDTO.toProductInOrderResponseDTO(products.get(i), orderRequestDTO.getProductsNumber().get(i)));
        }

        Order savedOrder = orderRepository.save(order);

        return OrderResponseDTO.toOrderResponseDTO(savedOrder,productsResponseDTO);
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

                productDTO = ProductInOrderResponseDTO.toProductInOrderResponseDTO(product, productNum);

                products.add(productDTO);
            }

            OrderResponseDTO orderDTO = OrderResponseDTO.toOrderResponseDTO(order, products);
            orders.add(orderDTO);
        }

        return orders;
    }
}
