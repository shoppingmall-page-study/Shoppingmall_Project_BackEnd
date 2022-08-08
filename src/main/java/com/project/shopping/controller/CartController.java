package com.project.shopping.controller;

import com.project.shopping.auth.PrincipalDetails;
import com.project.shopping.dto.CartDTO;
import com.project.shopping.dto.ProductDTO;
import com.project.shopping.dto.ResponseDTO;
import com.project.shopping.dto.UserDTO;
import com.project.shopping.model.Cart;
import com.project.shopping.model.Product;
import com.project.shopping.model.User;
import com.project.shopping.service.CartService;
import com.project.shopping.service.ProductService;
import com.project.shopping.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class CartController {
    @Autowired
    private  CartService cartService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    // 상품 생성
    // product id를 받아서 해당 상품을 찾고 인증객체를 통한 user 을 받아 장바구니 생성
    @GetMapping("/cart/create/{id}")
    // 여기서 ID는 상품 ID
    public ResponseEntity<?> createCart(Authentication authentication, @PathVariable(value = "id") int ProductId){
        try{
            PrincipalDetails userDetails = (PrincipalDetails) authentication.getPrincipal();
            String email = userDetails.getUser().getEmail();
            User user = userService.findEmailByUser(email); // user 찾기
            Product product = productService.findproductid(ProductId); // 상품 찾기
            Cart cart = Cart.builder().productId(product).userId(user).createTime(Timestamp.valueOf(LocalDateTime.now())).build();
            cartService.create(cart); // 카트 생성



            return ResponseEntity.ok().body("");
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }


    }
    // 장바구니 삭제


    // 장바구니 해당 아이디

    @GetMapping("/cart/list")
    public ResponseEntity<?> cartlist(Authentication authentication){
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        String userEmail = principalDetails.getUser().getEmail();
        User user = userService.findEmailByUser(userEmail);
        List<Cart> cartList = cartService.findallByUserId(user);
        List<CartDTO> cartdtos = new ArrayList<>();
        for(Cart cart: cartList){
            CartDTO cartDto = CartDTO.builder()
                    .userId(cart.getUserId().getUserId())
                    .userEmail(cart.getUserId().getEmail())
                    .userNickName(cart.getUserId().getNickname())
                    .userName(cart.getUserId().getUsername())
                    .userAddress(cart.getUserId().getAddress())
                    .userAge(cart.getUserId().getAge())
                    .userPhoneNumber(cart.getUserId().getPhoneNumber())
                    .productName(cart.getProductId().getName())
                    .productId(cart.getProductId().getId())
                    .productPrice(cart.getProductId().getPrice())
                    .productTotal(cart.getProductId().getTotal())
                    .imgUrl(cart.getProductId().getImgUrl())
                    .createTime(cart.getCreateTime()).build();

            cartdtos.add(cartDto);

        }
        Map<String, Object> result = new HashMap<>();
        result.put("data", cartdtos);
        return  ResponseEntity.ok().body(result);


    }

    // 삭제 쿼리
    @DeleteMapping("/cart/delete/{id}")
    public ResponseEntity<?> cartdelete(Authentication authentication, @PathVariable(value = "id")int id){
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        String email = principalDetails.getUser().getEmail();

        User user = userService.findEmailByUser(email);
        Cart findcart = cartService.findCartUserAndId(user, id);
        cartService.deleteCart(findcart);

        CartDTO cartDTO = CartDTO.builder()
                .userEmail(user.getEmail())
                .productName(findcart.getProductId().getName())
                .build();
        return  ResponseEntity.ok().body(cartDTO);
    }




}
