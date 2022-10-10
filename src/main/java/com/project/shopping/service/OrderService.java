package com.project.shopping.service;

import com.project.shopping.model.Cart;
import com.project.shopping.model.Order;
import com.project.shopping.model.Product;
import com.project.shopping.model.User;
import com.project.shopping.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    public Order create(Order order){
        if(order.getProducts() == null){
            log.warn("product not found", order.getProducts());
            throw new RuntimeException("product not found");
        }
/*        if(orderRepository.existsById(order.getId())){
            log.warn("order already exists", order.getId());
            throw new RuntimeException("order already exists");
        }*/

        return orderRepository.save(order);
    }

    public Order update(Order order){
        if(order.getProducts()== null){
            throw new NoSuchElementException("해당 상품이 없습니다.");
        }
        return orderRepository.save(order);
    }

    public Order findById(int id){
        Order order = orderRepository.findById(id)
            .orElseThrow(()->new
                    IllegalArgumentException("order Not Found."));
        return order;
    }

    public List<Order> findAllByUserId(User user){
        return orderRepository.findAllByUser(user);
    }
}
