package com.project.shopping.controller;


import com.project.shopping.Error.CustomException;
import com.project.shopping.Error.ErrorCode;
import com.project.shopping.auth.PrincipalDetails;
import com.project.shopping.dto.requestDTO.ProductRequestDTO.ProductCreateRequestDTO;
import com.project.shopping.dto.requestDTO.ProductRequestDTO.ProductSearchRequestDTO;
import com.project.shopping.dto.requestDTO.ProductRequestDTO.ProductUpdateRequestDTO;
import com.project.shopping.dto.responseDTO.ProductResponseDTO.*;
import com.project.shopping.model.Product;
import com.project.shopping.model.User;
import com.project.shopping.repository.ProductRepository;
import com.project.shopping.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    private final  ProductService productService;

    private String ActiveStatus= "active";


    @PostMapping("/api/product/create")
    public ResponseEntity<?> createProduct(Authentication authentication, @RequestBody @Valid ProductCreateRequestDTO productCreateRequestDTO) {

        User user = ((PrincipalDetails) authentication.getPrincipal()).getUser();

        ProductCreateResponseDTO productCreateResponseDTO = productService.create(productCreateRequestDTO,user); // 상품 생성

        Map<String, Object> result = new HashMap<>();
        result.put("msg", "상품 등록에 성공했습니다.");
        result.put("data", productCreateResponseDTO);
        return ResponseEntity.ok().body(result);
    }


    @DeleteMapping("/api/product/delete/{id}")
    public ResponseEntity<?>  productDelete(Authentication authentication, @PathVariable(value = "id") int product){

        User user = ((PrincipalDetails) authentication.getPrincipal()).getUser();

        ProductDeleteResponseDTO productDeleteResponseDTO = productService.deleteProduct(user, product);

        Map<String, Object> result = new HashMap<>();
        result.put("msg", "상품삭제에 성공했습니다.");
        result.put("data", productDeleteResponseDTO);

        return ResponseEntity.ok().body(result);

    }

    @GetMapping("/api/products")
    public ResponseEntity<?> findAll(){
        List<ProductJoinResponseDTO> productJoinResponseDTOS = productService.getActiveProdcutList(ActiveStatus);

        Map<String , Object> result = new HashMap<>();
        result.put("msg","상품검색에 성공했습니다.");
        result.put("data",productJoinResponseDTOS);
        return ResponseEntity.ok().body(result);

    }

    @PostMapping("/api/product/search")
    public ResponseEntity<?> searchProduct(@RequestBody @Valid ProductSearchRequestDTO productSearchRequestDTO){
        List<ProductSearchResponseDTO> productSearchResponseDTOS = productService.getProductList(productSearchRequestDTO.getKeyword(), ActiveStatus);
        Map<String , Object> result = new HashMap<>();
        result.put("msg","상품검색에 성공했습니다.");
        result.put("data",productSearchResponseDTOS);
        return ResponseEntity.ok().body(result);

    }

    // 내가 올린 상품 검색
    @GetMapping("/api/products/user")
    public ResponseEntity<?> findResistedProductByUser(Authentication authentication){

        User user = ((PrincipalDetails) authentication.getPrincipal()).getUser();

        List<ProductJoinResponseDTO> productJoinResponseDTOS = productService.getEqUserAndActive(user, ActiveStatus); // 해당 유저가 등록한 상품들 찾기


        Map<String , Object> result = new HashMap<>();
        result.put("msg","상품 조회에 성공했습니다.");
        result.put("data",productJoinResponseDTOS);
        return ResponseEntity.ok().body(result);

    }

    @GetMapping("/api/product/{id}")
    public ResponseEntity<?>  productFind(@PathVariable(value = "id") int product){
        //service
        ProductJoinResponseDTO productJoinResponseDTO = productService.findById(product);

        Map<String, Object> result = new HashMap<>();
        result.put("msg","상품검색에 성공했습니다.");
        result.put("data", productJoinResponseDTO);

        return ResponseEntity.ok().body(result);
    }

    @PutMapping("/api/product/update/{id}")
    public ResponseEntity<?> updateProduct(Authentication authentication, @PathVariable(value = "id") int product, @RequestBody @Valid ProductUpdateRequestDTO productUpdateRequestDTO){

        User user = ((PrincipalDetails) authentication.getPrincipal()).getUser();
        ProductUpdateResponseDTO productUpdateResponseDTO = productService.update(user, productUpdateRequestDTO, product);

        Map<String , Object> result = new HashMap<>();
        result.put("msg","상품 수정에 성공했습니다.");
        result.put("data",productUpdateResponseDTO);

        return ResponseEntity.ok().body(result);
    }

}