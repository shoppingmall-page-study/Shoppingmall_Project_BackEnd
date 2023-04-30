package com.project.shopping.repository;

import com.project.shopping.model.Order;
import com.project.shopping.model.OrderDetail;
import com.project.shopping.model.Product;
import com.project.shopping.model.QOrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {

    List<OrderDetail> findOrderDetailByOrderId(Order order);
}
