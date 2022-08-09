package com.project.shopping.service;

import com.project.shopping.model.Cart;
import com.project.shopping.model.User;
import com.project.shopping.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {
    @Autowired
    private CartRepository cartRepository;

    public Cart create(Cart cart){
        return cartRepository.save(cart);
    }

    public  void deleteCart(Cart cart){cartRepository.delete(cart);}
    public  List<Cart> findallByUserId(User user){return cartRepository.findAllByuserId(user); }

    public Cart findCartUserAndId(User user, int id){
        return cartRepository.findCartByUserIdAndId(user, id);
    }
}
