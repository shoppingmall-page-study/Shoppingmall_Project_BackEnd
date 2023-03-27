package com.project.shopping.controller;

import com.project.shopping.Error.CustomException;
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
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class CartController {

    private  final CartService cartService;



    private  String ActiveStatus = "active";

    // 상품 생성
    // product id를 받아서 해당 상품을 찾고 인증객체를 통한 user 을 받아 장바구니 생성
    @PostMapping("/api/cart/create/{id}")
    // 여기서 ID는 상품 ID
    public ResponseEntity<?> createCart(Authentication authentication, @PathVariable(value = "id") int ProductId ,@RequestBody @Valid CartCreateRequestDTO cartCreateRequestDTO){

        User user = ((PrincipalDetails) authentication.getPrincipal()).getUser();

        CartCreateResponseDTO cartCreateResponseDTO = cartService.create(user, ProductId, cartCreateRequestDTO); // 카트 생성
        Map<String, Object> result = new HashMap<>();
        result.put("msg", "장바구니 등록에 성공했습니다.");
        result.put("data", cartCreateResponseDTO);

        // 유저가 장바구니에 담은 상품이 기존에 있을시
        return  ResponseEntity.ok().body(result);
    }
    // 장바구니 삭제


    // 장바구니 해당 아이디


    @GetMapping("/api/cart/user")
    public ResponseEntity<?> cartList(Authentication authentication){

        User user = ((PrincipalDetails) authentication.getPrincipal()).getUser();

        List<CartUserListJoinResponseDTO> cartUserListJoinResponseDTOS = cartService.getEqUserAndCart(user, ActiveStatus);

        Map<String, Object> result = new HashMap<>();
        result.put("msg", " 장바구니 조회에 성공했습니다.");
        result.put("data",cartUserListJoinResponseDTOS);


        return  ResponseEntity.ok().body(result);
    }

    // 삭제 쿼리
    @DeleteMapping("/api/cart/delete/{id}") // id는 cart_Id
    public ResponseEntity<?> cartDelete(Authentication authentication, @PathVariable(value = "id")int id){

        User user = ((PrincipalDetails) authentication.getPrincipal()).getUser();

        CartDeleteResponseDTO cartDeleteResponseDTO = cartService.deleteCart(user, id);

        Map<String, Object> result = new HashMap<>();
        result.put("msg","장바구니 삭제에 성공했습니다.");
        result.put("data", cartDeleteResponseDTO);
        return  ResponseEntity.ok().body(result);
    }

    @PutMapping("/api/cart/update/{id}")
    public ResponseEntity<?> cartUpdate(Authentication authentication, @PathVariable(value = "id") int cartId , @RequestBody @Valid CartUpdateRequestDTO cartUpdateRequestDTO){

        User user = ((PrincipalDetails) authentication.getPrincipal()).getUser();

        CartUpdateResosneDTO cartUpdateResosneDTO = cartService.update(user, cartId, cartUpdateRequestDTO);

        Map<String, Object> result = new HashMap<>();
        result.put("msg","장바구니 수정에 성공했습니다.");
        result.put("data", cartUpdateResosneDTO);

        return  ResponseEntity.ok().body(result);


    }

}
