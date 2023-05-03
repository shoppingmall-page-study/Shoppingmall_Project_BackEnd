package com.project.shopping.service;

import com.project.shopping.Error.CustomException;
import com.project.shopping.Error.ErrorCode;
import com.project.shopping.dto.requestDTO.CartRequestDTO.CartCreateRequestDTO;
import com.project.shopping.dto.requestDTO.CartRequestDTO.CartUpdateRequestDTO;
import com.project.shopping.dto.responseDTO.CartResponseDTO.*;
import com.project.shopping.model.Cart;
import com.project.shopping.model.Product;
import com.project.shopping.model.User;
import com.project.shopping.repository.CartRepository;
import com.project.shopping.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CartService {

    private  final CartRepository cartRepository;
    private  final ProductRepository productRepository;

    //생성
    public  CartCreateResponseDTO create(User user, int product, CartCreateRequestDTO cartCreateRequestDTO){

        Product findProduct = productRepository.findById(product)
                .orElseThrow(()-> new CustomException(ErrorCode.NotFoundProductException));// 상품 찾기


        if(cartRepository.existsCartByUserAndProductAndStatus(user, findProduct,"active")){ // 해당 상품이 존재 할시
            Cart findCart = cartRepository.findCartByUserAndProduct(user, findProduct)
                    .orElseThrow(()-> new CustomException(ErrorCode.NotFoundCartException));// 카트찾기

            long totalSum = findCart.getProductNum()+ cartCreateRequestDTO.getProductNum();

            if(totalSum> findProduct.getTotal()){
                throw  new CustomException(ErrorCode.NotFoundCartNumException);

            }

            findCart.setProductNum(totalSum);

            Cart createCart = cartRepository.save(findCart); // 카트 생성

            ProductResponseDTO productResponseDTO = ProductResponseDTO.toProductResponseDTO(createCart.getProduct());

            return CartCreateResponseDTO.toCartCreateResponseDTO(createCart, productResponseDTO);

        }else{
            Cart cart = cartCreateRequestDTO.toEntity(findProduct, user);
            Cart createCart = cartRepository.save(cart); // 카트 생성
            ProductResponseDTO productResponseDTO = ProductResponseDTO.toProductResponseDTO(createCart.getProduct());
            return CartCreateResponseDTO.toCartCreateResponseDTO(createCart, productResponseDTO);
        }

    }

    public CartUpdateResponseDTO update(User user, int cartId, CartUpdateRequestDTO cartUpdateRequestDTO){

        Cart cart = cartRepository.findCartByUserAndId(user,cartId)
                .orElseThrow(()-> new CustomException(ErrorCode.NotFoundCartException));// 유저랑 카트 아이디를 이용한 카트 찾기
        long totalCount =  (cartUpdateRequestDTO.getProductNum());

        if(totalCount > cart.getProduct().getTotal()){
            throw new CustomException(ErrorCode.NotFoundCartNumException);
        }
        if(totalCount <1){
            throw new CustomException(ErrorCode.NotFoundCartNumDownException);
        }

        cart.setProductNum(cartUpdateRequestDTO.getProductNum());

        cartRepository.save(cart);


        ProductResponseDTO productResponseDTO = ProductResponseDTO.toProductResponseDTO(cart.getProduct());

        return CartUpdateResponseDTO.toCartUpdateResponseDTO(cart, productResponseDTO);
    }

    // 삭제
    public  CartDeleteResponseDTO deleteCart(User user, int id){

        Cart findcart = cartRepository.findCartByUserAndId(user, id)
                        .orElseThrow(()-> new CustomException(ErrorCode.NotFoundCartException));
        findcart.setStatus("Disabled");

        cartRepository.save(findcart);
        ProductResponseDTO productResponseDTO = ProductResponseDTO.toProductResponseDTO(findcart.getProduct());

        return CartDeleteResponseDTO.toCartDeleteResponseDTO(findcart, productResponseDTO);
    }


    public List<CartUserListJoinResponseDTO>  getEqUserAndCart(User user, String status){

        List<Cart> cartList =  cartRepository.getEqUserAndCart(user, status);
        List<CartUserListJoinResponseDTO> cartdtos = new ArrayList<>();

        for(Cart cart: cartList){
            ProductResponseDTO productResponseDTO = ProductResponseDTO.toProductResponseDTO(cart.getProduct());

            CartUserListJoinResponseDTO cartUserListJoinResponseDTO = CartUserListJoinResponseDTO.toCartUserListJoinResponseDTO(cart, productResponseDTO);

            cartdtos.add(cartUserListJoinResponseDTO);
        }

        return cartdtos;

    }

}
