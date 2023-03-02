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
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CartService {

    private  final CartRepository cartRepository;
    private  final ProductRepository productRepository;
    private  final UserRepository userRepository;

    //생성
    public  CartCreateResponseDTO create(Authentication authentication, int ProductId, CartCreateRequestDTO cartCreateRequestDTO){
        PrincipalDetails userDetails = (PrincipalDetails) authentication.getPrincipal();
        String email = userDetails.getUser().getEmail();
        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new CustomException(ErrorCode.NotFoundUserException));// user 찾기
        Product product = productRepository.findById(ProductId)
                .orElseThrow(()-> new CustomException(ErrorCode.NotFoundProductException));// 상품 찾기


        if(cartRepository.existsCartByUserIdAndProductIdAndStatus(user, product,"active")){ // 해당 상품이 존재 할시
            Cart findCart = cartRepository.findCartByUserIdAndProductId(user, product)
                    .orElseThrow(()-> new CustomException(ErrorCode.NotFoundCartException));// 카트찾기
            long totalsum = findCart.getProductNum()+ cartCreateRequestDTO.getProductNum();
            //System.out.println(totalsum);
            if(totalsum> product.getTotal()){
                throw  new CustomException(ErrorCode.NotFoundCartNumException);
            }
            findCart.setProductNum(totalsum);
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

    public CartUpdateResosneDTO update(Authentication authentication, int cartId, CartUpdateRequestDTO cartUpdateRequestDTO){
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        String email = principalDetails.getUser().getEmail();
        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new CustomException(ErrorCode.NotFoundUserException));// 유저 찾기

        Cart cart = cartRepository.findCartByUserIdAndId(user,cartId)
                .orElseThrow(()-> new CustomException(ErrorCode.NotFoundCartException));// 유저랑 카트 아이디를 이용한 카트 찾기
        long totalCount =  (cartUpdateRequestDTO.getProductNum());

        //System.out.println(totalcount);
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
    public  CartDeleteResponseDTO deleteCart(Authentication authentication, int id){
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        String email = principalDetails.getUser().getEmail();

        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new CustomException(ErrorCode.NotFoundUserException));
//        if(!cartRepository.existsByUserIdAndId(user, id)){
//            throw  new CustomException("Cart Not Found",ErrorCode.NotFoundCartException);
//        } // 밑에 코드랑 같은 코드 임

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
    // user가 등록한 장바구니 목록 조회
    public  List<Cart> findallByUserId(User user){return cartRepository.findAllByuserId(user); }

    // 유저  와 cartid를 이용한 해당 장바구니 상품 조회
    public Cart findCartUserAndId(User user, int id){
        return cartRepository.findCartByUserIdAndId(user, id)
                .orElseThrow(()-> new CustomException(ErrorCode.NotFoundCartException));
    }

    public  boolean existCartUserAndId(User user, int id){return  cartRepository.existsByUserIdAndId(user, id);}


    public boolean existsCartByUserIdAndProductId(User user, Product product){return cartRepository.existsCartByUserIdAndProductIdAndStatus(user,product,"active");}

    public Cart findCartByUserIdAndProductId(User user, Product product){return  cartRepository.findCartByUserIdAndProductId(user,product)
            .orElseThrow(()->new CustomException(ErrorCode.NotFoundCartException));
    }

    public List<CartUserListJoinResponseDTO>  getEqUserAndCart(Authentication authentication, String status){

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        String userEmail = principalDetails.getUser().getEmail();

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(()-> new CustomException(ErrorCode.NotFoundUserException));


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
