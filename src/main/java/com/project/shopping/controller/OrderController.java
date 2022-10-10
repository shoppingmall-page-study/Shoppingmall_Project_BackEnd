package com.project.shopping.controller;

import com.project.shopping.auth.PrincipalDetails;
import com.project.shopping.dto.requestDTO.OrderRequestDTO.OrderRequestDTO;
import com.project.shopping.dto.responseDTO.OrderResponseDTO.OrderResponseDTO;
import com.project.shopping.dto.responseDTO.OrderResponseDTO.ProductInOrderResponseDTO;
import com.project.shopping.model.Order;
import com.project.shopping.model.OrderDetail;
import com.project.shopping.model.Product;
import com.project.shopping.model.User;
import com.project.shopping.service.OrderDetailService;
import com.project.shopping.service.OrderService;
import com.project.shopping.service.ProductService;
import com.project.shopping.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;
    private final ProductService productService;
    private final OrderDetailService orderDetailService;

    @GetMapping("/api/order")
    public ResponseEntity<?> getOrderList(Authentication authentication){
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        User user = principalDetails.getUser();

        List<Order> orderList = orderService.findAllByUserId(user);
        ArrayList<OrderResponseDTO> orders = new ArrayList<>();

        for(Order order : orderList){
            ArrayList<ProductInOrderResponseDTO> products = new ArrayList<>();

            for(int i = 0; i < order.getProducts().size(); i++){
                ProductInOrderResponseDTO productDTO = new ProductInOrderResponseDTO();
                Product product = order.getProducts().get(i).getProduct();
                int productNum = order.getProducts().get(i).getProductNum();

                productDTO = ProductInOrderResponseDTO.builder()
                        .productId(product.getId())
                        .productName(product.getName())
                        .productNum(productNum)
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
        Map<String , Object> result = new HashMap<>();
        result.put("data", orders);

        return ResponseEntity.ok().body(result);
    }


    @PostMapping("/api/order/create")
    public ResponseEntity<?> createOrder(Authentication authentication, @RequestBody OrderRequestDTO orderDTO){
        try{
            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
            String email = principalDetails.getUser().getEmail();
            User user = userService.findEmailByUser(email);
            ArrayList<OrderDetail> products = new ArrayList<>();
            ArrayList<ProductInOrderResponseDTO>  responseProductsDTO = new ArrayList<>();
            long totalAmount = 0;
            Order order = Order.builder()
                    .products(products)
                    .user(user)
                    .orderComplete("ready")
                    .status("active")
                    .build();

            for(int i = 0; i < orderDTO.getProductsId().size(); i++){
                Product product = productService.findproductid(orderDTO.getProductsId().get(i));
                int productNum = orderDTO.getProductsNumber().get(i);

                OrderDetail orderDetail = OrderDetail.builder()
                        .product(product)
                        .productNum(productNum)
                        .build();

                ProductInOrderResponseDTO productDTO = ProductInOrderResponseDTO.builder()
                        .productId(product.getId())
                        .productName(product.getName())
                        .productNum(productNum)
                        .build();

                order.addProduct(orderDetail);
                responseProductsDTO.add(productDTO);
                totalAmount += product.getPrice() * productNum;
            }

            order.setAmount(totalAmount);
            Order savedOrder = orderService.create(order);
            OrderResponseDTO orderResponseDTO = OrderResponseDTO.builder()
                    .orderId(savedOrder.getId())
                    .products(responseProductsDTO)
                    .orderComplete(savedOrder.getOrderComplete())
                    .amount(savedOrder.getAmount())
                    .orderTime(savedOrder.getOrderTime())
                    .build();


            Map<String, Object> result = new HashMap<>();
            result.put("msg", "주문 등록에 성공했습니다.");
            result.put("data", orderResponseDTO);

            return ResponseEntity.ok().body(result);
        }
        catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
