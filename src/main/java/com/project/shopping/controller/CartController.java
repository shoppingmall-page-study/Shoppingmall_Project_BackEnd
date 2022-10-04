package com.project.shopping.controller;

import com.project.shopping.Error.CustomExcpetion;
import com.project.shopping.Error.ErrorCode;
import com.project.shopping.auth.PrincipalDetails;
import com.project.shopping.dto.*;
import com.project.shopping.dto.requestDTO.CartRequestDTO.CartCreateRequestDTO;
import com.project.shopping.dto.requestDTO.CartRequestDTO.CartUpdateRequestDTO;
import com.project.shopping.dto.responseDTO.CartResponseDTO.*;
import com.project.shopping.model.Cart;
import com.project.shopping.model.Product;
import com.project.shopping.model.User;
import com.project.shopping.service.CartService;
import com.project.shopping.service.ProductService;
import com.project.shopping.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

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
    @PostMapping("/api/cart/create/{id}")
    // 여기서 ID는 상품 ID
    public ResponseEntity<?> createCart(Authentication authentication, @PathVariable(value = "id") int ProductId ,@RequestBody CartCreateRequestDTO cartCreateRequestDTO){
        if(authentication == null){
            throw  new CustomExcpetion("허용되지 않은 접근입니다." , ErrorCode.UnauthorizedException);
        }
        PrincipalDetails userDetails = (PrincipalDetails) authentication.getPrincipal();
        String email = userDetails.getUser().getEmail();
        User user = userService.findEmailByUser(email); // user 찾기
        Product product = productService.findproductid(ProductId); // 상품 찾기
        System.out.println(cartCreateRequestDTO.getProductNum()); // 현재 장바구니 개수 로그 

        if(cartCreateRequestDTO.getProductNum() > product.getTotal()){
            throw  new CustomExcpetion("상품이 존재하지 않습니다", ErrorCode.NotFoundProductException);
        }

        // 유저가 장바구니에 담은 상품이 기존에 있을시
        if(cartService.existsCartByUserIdAndProductId(user, product)){ // 해당 상품이 존재 할시
            Cart findCart = cartService.findCartByUserIdAndProductId(user, product); // 카트찾기
            long totalsum = findCart.getProductNum()+ cartCreateRequestDTO.getProductNum();
            //System.out.println(totalsum);
            if(totalsum> product.getTotal()){
                throw  new CustomExcpetion("상품개수가 초과하였습니다.",ErrorCode.NotFoundCartNumException);
            }
            findCart.setProductNum(totalsum);
            Cart createcart = cartService.create(findCart); // 카트 생성
            ProductReponseDTO productReponseDTO = ProductReponseDTO.builder()
                    .productId(createcart.getProductId().getId())
                    .title(createcart.getProductId().getTitle())
                    .name(createcart.getProductId().getName())
                    .content(createcart.getProductId().getContent())
                    .imgUrl(createcart.getProductId().getImgUrl())
                    .price(createcart.getProductId().getPrice())
                    .build();

            CartCreateResponseDTO cartCreateResponseDTO = CartCreateResponseDTO
                    .builder()
                    .cartId(createcart.getId())
                    .product(productReponseDTO)
                    .productNum(createcart.getProductNum())
                    .createDate(createcart.getCreateTime())
                    .build();
            Map<String, Object> result = new HashMap<>();
            result.put("msg", "장바구니 등록에 성공했습니다.");
            result.put("data", cartCreateResponseDTO);
            return ResponseEntity.ok().body(cartCreateResponseDTO);

        }else{
            Cart cart = Cart.builder()
                    .productId(product)
                    .userId(user)
                    .createTime(Timestamp.valueOf(LocalDateTime.now()))
                    .productNum(cartCreateRequestDTO.getProductNum())
                    .status("active")
                    .build();
            Cart createcart = cartService.create(cart); // 카트 생성
            ProductReponseDTO productReponseDTO = ProductReponseDTO.builder()
                    .productId(createcart.getProductId().getId())
                    .title(createcart.getProductId().getTitle())
                    .name(createcart.getProductId().getName())
                    .content(createcart.getProductId().getContent())
                    .imgUrl(createcart.getProductId().getImgUrl())
                    .price(createcart.getProductId().getPrice())
                    .build();
            CartCreateResponseDTO cartCreateResponseDTO = CartCreateResponseDTO
                    .builder()
                    .cartId(createcart.getId())
                    .product(productReponseDTO)
                    .productNum(createcart.getProductNum())
                    .createDate(createcart.getCreateTime())
                    .build();



            Map<String, Object> result = new HashMap<>();
            result.put("msg", "장바구니 등록에 성공했습니다.");
            result.put("data", cartCreateResponseDTO);
            return ResponseEntity.ok().body(cartCreateResponseDTO);
        }



    }
    // 장바구니 삭제


    // 장바구니 해당 아이디

    private  String ActiveStatus = "active";
    @GetMapping("/api/cart/user")
    public ResponseEntity<?> cartlist(Authentication authentication){
        if(authentication == null){
            throw  new CustomExcpetion("허용되지 않은 접근입니다." , ErrorCode.UnauthorizedException);
        }
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        String userEmail = principalDetails.getUser().getEmail();
        User user = userService.findEmailByUser(userEmail);
        List<Cart> cartList = cartService.getEqUserAndCart(user,ActiveStatus);
        List<CartUserListJoinResponseDTO> cartdtos = new ArrayList<>();
        long totalsum = 0; // 최종 합계
        int totalcarttotal =0;
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
                    .createDate(cart.getCreateTime())
                    .build();

            cartdtos.add(cartUserListJoinResponseDTO);
            // totalsum  = (totalsum + (cart.getProductNum() * cart.getProductId().getPrice()));
            // totalcarttotal = (int) (totalcarttotal +cart.getProductNum());

        }
        Map<String, Object> result = new HashMap<>();
        result.put("msg", " 장바구니 조회에 성공했습니다.");
        result.put("data",cartdtos);
        // list.size

        //System.out.println(totalsum);


        return  ResponseEntity.ok().body(result);


    }

    // 삭제 쿼리
    @DeleteMapping("/api/cart/delete/{id}") // id는 cart_Id
    public ResponseEntity<?> cartdelete(Authentication authentication, @PathVariable(value = "id")int id){
        if(authentication == null){
            throw  new CustomExcpetion("허용되지 않은 접근입니다." , ErrorCode.UnauthorizedException);
        }
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        String email = principalDetails.getUser().getEmail();

        User user = userService.findEmailByUser(email);
        if(!cartService.existCartUserAndId(user, id)){
            throw  new CustomExcpetion("해당 상품이 존재하지 않습니다.",ErrorCode.NotFoundProductException);
        }
        Cart findcart = cartService.findCartUserAndId(user, id);
        findcart.setStatus("Disabled");
        cartService.update(findcart);

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
                .createDate(findcart.getCreateTime())
                .build();

        Map<String, Object> result = new HashMap<>();
        result.put("msg","장바구니 삭제에 성공했습니다.");
        result.put("data", cartDeleteResponseDTO);
        return  ResponseEntity.ok().body(result);
    }

    @PutMapping("/api/cart/update/{id}")
    public ResponseEntity<?> cartupdate(Authentication authentication, @PathVariable(value = "id") int cartId , @RequestBody CartUpdateRequestDTO cartUpdateRequestDTO){

        if(authentication == null){
            throw  new CustomExcpetion("허용되지 않은 접근입니다." , ErrorCode.UnauthorizedException);
        }
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        String email = principalDetails.getUser().getEmail();
        User user = userService.findEmailByUser(email); // 유저 찾기

        Cart cart = cartService.findCartUserAndId(user,cartId); // 유저랑 카트 아이디를 이용한 카트 찾기
        long totalcount =  (cartUpdateRequestDTO.getProductNum());

        //System.out.println(totalcount);
        if(totalcount > cart.getProductId().getTotal()){
            throw new CustomExcpetion("상품의 구매한도를 초과했습니다.", ErrorCode.NotFoundCartNumException);
        }
        if(totalcount <1){
            throw new CustomExcpetion("상품의 구매한도를 초과했습니다.", ErrorCode.NotFoundCartNumDownException);
        }

        cart.setProductNum(cartUpdateRequestDTO.getProductNum());
        Cart updatecart = cartService.update(cart);
//            System.out.println(cartUpdateRequestDTO.getProductNum() + "cartcount"); // 1
//            System.out.println(totalcount+ "totalcount");
//            System.out.println(updatecart.getProductNum() + "update");

        ProductReponseDTO productReponseDTO = ProductReponseDTO.builder()
                .productId(updatecart.getProductId().getId())
                .title(updatecart.getProductId().getTitle())
                .name(updatecart.getProductId().getName())
                .content(updatecart.getProductId().getContent())
                .imgUrl(updatecart.getProductId().getImgUrl())
                .price(updatecart.getProductId().getPrice())
                .build();

        CartUpdateResosneDTO cartUpdateResosneDTO  = CartUpdateResosneDTO.builder()
                .cartId(updatecart.getId())
                .product(productReponseDTO)
                .productNum(updatecart.getProductNum())
                .createDate(updatecart.getCreateTime())
                .build();

        Map<String, Object> result = new HashMap<>();
        result.put("msg","장바구니 수정에 성공했습니다.");
        result.put("data", cartUpdateResosneDTO);

        return  ResponseEntity.ok().body(result);



    }





}
