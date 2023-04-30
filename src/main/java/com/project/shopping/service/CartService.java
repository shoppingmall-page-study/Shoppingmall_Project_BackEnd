package com.project.shopping.service;

import com.project.shopping.Error.CustomException;
import com.project.shopping.Error.ErrorCode;
import com.project.shopping.auth.PrincipalDetails;
import com.project.shopping.dto.requestDTO.CartRequestDTO.CartCreateRequestDTO;
import com.project.shopping.dto.requestDTO.CartRequestDTO.CartUpdateRequestDTO;
import com.project.shopping.dto.responseDTO.CartResponseDTO.*;
import com.project.shopping.model.Cart;
import com.project.shopping.model.Product;
import com.project.shopping.model.User;
import com.project.shopping.repository.CartRepository;
import com.project.shopping.repository.ProductRepository;
import com.project.shopping.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CartService {

    private  final CartRepository cartRepository;
    private  final ProductRepository productRepository;
    private  final UserRepository userRepository;

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

            ProductReponseDTO productReponseDTO = ProductReponseDTO.builder()
                    .productId(createCart.getProduct().getId())
                    .title(createCart.getProduct().getTitle())
                    .name(createCart.getProduct().getName())
                    .content(createCart.getProduct().getContent())
                    .imgUrl(createCart.getProduct().getImgUrl())
                    .price(createCart.getProduct().getPrice())
                    .build();

            CartCreateResponseDTO cartCreateResponseDTO = CartCreateResponseDTO
                    .builder()
                    .cartId(createCart.getId())
                    .product(productReponseDTO)
                    .productNum(createCart.getProductNum())
                    .createDate(createCart.getCreateDate())
                    .build();

            return cartCreateResponseDTO;

        }else{
            Cart cart = Cart.builder()
                    .product(findProduct)
                    .user(user)
                    .productNum(cartCreateRequestDTO.getProductNum())
                    .status("active")
                    .build();
            Cart createCart = cartRepository.save(cart); // 카트 생성
            ProductReponseDTO productReponseDTO = ProductReponseDTO.builder()
                    .productId(createCart.getProduct().getId())
                    .title(createCart.getProduct().getTitle())
                    .name(createCart.getProduct().getName())
                    .content(createCart.getProduct().getContent())
                    .imgUrl(createCart.getProduct().getImgUrl())
                    .price(createCart.getProduct().getPrice())
                    .build();
            CartCreateResponseDTO cartCreateResponseDTO = CartCreateResponseDTO
                    .builder()
                    .cartId(createCart.getId())
                    .product(productReponseDTO)
                    .productNum(createCart.getProductNum())
                    .createDate(createCart.getCreateDate())
                    .build();


            return cartCreateResponseDTO;
        }

    }

    public CartUpdateResosneDTO update(User user, int cartId, CartUpdateRequestDTO cartUpdateRequestDTO){

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


        ProductReponseDTO productReponseDTO = ProductReponseDTO.builder()
                .productId(cart.getProduct().getId())
                .title(cart.getProduct().getTitle())
                .name(cart.getProduct().getName())
                .content(cart.getProduct().getContent())
                .imgUrl(cart.getProduct().getImgUrl())
                .price(cart.getProduct().getPrice())
                .build();

        CartUpdateResosneDTO cartUpdateResosneDTO  = CartUpdateResosneDTO.builder()
                .cartId(cart.getId())
                .product(productReponseDTO)
                .productNum(cart.getProductNum())
                .createDate(cart.getCreateDate())
                .build();
        return cartUpdateResosneDTO;
    }

    // 삭제
    public  CartDeleteResponseDTO deleteCart(User user, int id){

        Cart findcart = cartRepository.findCartByUserAndId(user, id)
                        .orElseThrow(()-> new CustomException(ErrorCode.NotFoundCartException));
        findcart.setStatus("Disabled");

        cartRepository.save(findcart);
        ProductReponseDTO productReponseDTO = ProductReponseDTO.builder()
                .productId(findcart.getProduct().getId())
                .title(findcart.getProduct().getTitle())
                .name(findcart.getProduct().getName())
                .content(findcart.getProduct().getContent())
                .imgUrl(findcart.getProduct().getImgUrl())
                .price(findcart.getProduct().getPrice())
                .build();

        CartDeleteResponseDTO cartDeleteResponseDTO = CartDeleteResponseDTO.builder()
                .cartId(findcart.getId())
                .product(productReponseDTO)
                .productNum(findcart.getProductNum())
                .createDate(findcart.getCreateDate())
                .build();



        return cartDeleteResponseDTO;


    }


    public List<CartUserListJoinResponseDTO>  getEqUserAndCart(User user, String status){

        List<Cart> cartList =  cartRepository.getEqUserAndCart(user, status);
        List<CartUserListJoinResponseDTO> cartdtos = new ArrayList<>();

        for(Cart cart: cartList){
            ProductReponseDTO productReponseDTO = ProductReponseDTO.builder()
                    .productId(cart.getProduct().getId())
                    .title(cart.getProduct().getTitle())
                    .name(cart.getProduct().getName())
                    .content(cart.getProduct().getContent())
                    .imgUrl(cart.getProduct().getImgUrl())
                    .price(cart.getProduct().getPrice())
                    .build();
            CartUserListJoinResponseDTO cartUserListJoinResponseDTO = CartUserListJoinResponseDTO.builder()
                    .cartId(cart.getId())
                    .product(productReponseDTO)
                    .productNum(cart.getProductNum())
                    .createDate(cart.getCreateDate())
                    .build();

            cartdtos.add(cartUserListJoinResponseDTO);

        }

        return cartdtos;

    }

}
