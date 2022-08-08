package com.project.shopping.repository;

import com.project.shopping.model.Cart;
import com.project.shopping.model.Product;
import com.project.shopping.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface CartRepository extends JpaRepository<Cart,Integer> {
    List<Cart> findAll();

    List<Cart> findAllByuserId(User UserId);

    Cart findCartByUserIdAndId(User userId, int id);


}
