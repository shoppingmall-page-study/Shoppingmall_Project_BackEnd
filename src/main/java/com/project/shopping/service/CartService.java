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
    public  CartCreateResponseDTO create(User user, int ProductId, CartCreateRequestDTO cartCreateRequestDTO){

        Product product = productRepository.findById(ProductId)
                .orElseThrow(()-> new CustomException(ErrorCode.NotFoundProductException));// 상품 찾기


        if(cartRepository.existsCartByUserIdAndProductIdAndStatus(user, product,"active")){ // 해당 상품이 존재 할시
            Cart findCart = cartRepository.findCartByUserIdAndProductId(user, product)
                    .orElseThrow(()-> new CustomException(ErrorCode.NotFoundCartException));// 카트찾기

            long totalSum = findCart.getProductNum()+ cartCreateRequestDTO.getProductNum();

            if(totalSum> product.getTotal()){
                throw  new CustomException(ErrorCode.NotFoundCartNumException);

            }

            findCart.setProductNum(totalSum);

            Cart createCart = cartRepository.save(findCart); // 카트 생성

            ProductReponseDTO productReponseDTO = ProductReponseDTO.builder()
                    .productId(createCart.getProductId().getId())
                    .title(createCart.getProductId().getTitle())
                    .name(createCart.getProductId().getName())
                    .content(createCart.getProductId().getContent())
                    .imgUrl(createCart.getProductId().getImgUrl())
                    .price(createCart.getProductId().getPrice())
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
                    .productId(product)
                    .userId(user)
                    .productNum(cartCreateRequestDTO.getProductNum())
                    .status("active")
                    .build();
            Cart createCart = cartRepository.save(cart); // 카트 생성
            ProductReponseDTO productReponseDTO = ProductReponseDTO.builder()
                    .productId(createCart.getProductId().getId())
                    .title(createCart.getProductId().getTitle())
                    .name(createCart.getProductId().getName())
                    .content(createCart.getProductId().getContent())
                    .imgUrl(createCart.getProductId().getImgUrl())
                    .price(createCart.getProductId().getPrice())
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

        Cart cart = cartRepository.findCartByUserIdAndId(user,cartId)
                .orElseThrow(()-> new CustomException(ErrorCode.NotFoundCartException));// 유저랑 카트 아이디를 이용한 카트 찾기
        long totalCount =  (cartUpdateRequestDTO.getProductNum());

        if(totalCount > cart.getProductId().getTotal()){
            throw new CustomException(ErrorCode.NotFoundCartNumException);
        }
        if(totalCount <1){
            throw new CustomException(ErrorCode.NotFoundCartNumDownException);
        }

        cart.setProductNum(cartUpdateRequestDTO.getProductNum());

        cartRepository.save(cart);


        ProductReponseDTO productReponseDTO = ProductReponseDTO.builder()
                .productId(cart.getProductId().getId())
                .title(cart.getProductId().getTitle())
                .name(cart.getProductId().getName())
                .content(cart.getProductId().getContent())
                .imgUrl(cart.getProductId().getImgUrl())
                .price(cart.getProductId().getPrice())
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

        Cart findcart = cartRepository.findCartByUserIdAndId(user, id)
                        .orElseThrow(()-> new CustomException(ErrorCode.NotFoundCartException));
        findcart.setStatus("Disabled");

        cartRepository.save(findcart);
        ProductReponseDTO productReponseDTO = ProductReponseDTO.builder()
                .productId(findcart.getProductId().getId())
                .title(findcart.getProductId().getTitle())
                .name(findcart.getProductId().getName())
                .content(findcart.getProductId().getContent())
                .imgUrl(findcart.getProductId().getImgUrl())
                .price(findcart.getProductId().getPrice())
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
                    .productId(cart.getProductId().getId())
                    .title(cart.getProductId().getTitle())
                    .name(cart.getProductId().getName())
                    .content(cart.getProductId().getContent())
                    .imgUrl(cart.getProductId().getImgUrl())
                    .price(cart.getProductId().getPrice())
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
