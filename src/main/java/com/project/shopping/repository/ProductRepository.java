package com.project.shopping.repository;

import com.project.shopping.model.Product;
import com.project.shopping.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface ProductRepository extends JpaRepository<Product,Integer>,ProductRepositoryCustom {

    @Override
    List<Product> getProductList(String title, String status);

    @Override
    List<Product> getActiveProdcutList(String status);


    @Override
    List<Product> getEqUserAndActive(User user, String status);

    Optional<Product> findById(int id);
    Optional<Product> findByIdAndUser(int id, User user);
    boolean existsByIdAndUser(int id , User user);

    List<Product> findAll();
    List<Product> findAllByUser(User user);

}
