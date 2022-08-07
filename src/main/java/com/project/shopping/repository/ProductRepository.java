package com.project.shopping.repository;

import com.project.shopping.model.Product;
import com.project.shopping.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(readOnly = true)
public interface ProductRepository extends JpaRepository<Product,Integer> {


    Product findById(int id);
    Product findByIdAndUserId(int id, User user);

    List<Product> findAll();
}
