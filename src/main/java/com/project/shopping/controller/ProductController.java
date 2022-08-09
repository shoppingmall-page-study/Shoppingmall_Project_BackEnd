package com.project.shopping.controller;


import com.project.shopping.auth.PrincipalDetails;
import com.project.shopping.dto.ProductDTO;
import com.project.shopping.dto.ResponseDTO;
import com.project.shopping.model.Product;
import com.project.shopping.model.User;
import com.project.shopping.service.ProductService;
import com.project.shopping.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @PostMapping("/product/create")
    public ResponseEntity<?> createProduct(Authentication authentication, @RequestBody  ProductDTO productDTO){
//        System.out.println(productDTO.getTitle());
//        System.out.println(productDTO.getName());
//        System.out.println(productDTO.getPrice());
//        System.out.println(productDTO.getTotal());
//        System.out.println(productDTO.getImgUrl());


        try{
            PrincipalDetails userDtails = (PrincipalDetails) authentication.getPrincipal();
            String email = userDtails.getUser().getEmail();
            User user = userService.findEmailByUser(email);


            // 인증 값 기반으로 user 찾기

            Product product = Product.builder().userId(user)
                    .title(productDTO.getTitle())
                    .content(productDTO.getContent())
                    .name(productDTO.getName())
                    .price(productDTO.getPrice())
                    .total(productDTO.getTotal())
                    .imgUrl(productDTO.getImgUrl())
                    .createDate(Timestamp.valueOf(LocalDateTime.now())).build();
            Product registeredProduct = productService.create(product); // 상품 생성

            ProductDTO response = ProductDTO.builder()
                    .productId(registeredProduct.getId())
                    .useremail(registeredProduct.getUserId().getEmail())
                    .userId(registeredProduct.getUserId().getUserId())
                    .userName(registeredProduct.getUserId().getUsername())
                    .userPhoneNumber(registeredProduct.getUserId().getPhoneNumber())
                    .title(registeredProduct.getTitle())
                    .content(registeredProduct.getContent())
                    .name(registeredProduct.getName())
                    .price(registeredProduct.getPrice())
                    .total(registeredProduct.getTotal())
                    .imgUrl(registeredProduct.getImgUrl())
                    .createDate(registeredProduct.getCreateDate())
                    .build();
            return ResponseEntity.ok().body(response);
        }catch (Exception e){
            ResponseDTO response = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity.badRequest().body(response);
        }

    }

    @DeleteMapping("/product/delete/{id}")
    public ResponseEntity<?>  productdelete(Authentication authentication, @PathVariable(value = "id") int ProductId){


        try{
            PrincipalDetails userDtails = (PrincipalDetails) authentication.getPrincipal();
            String email = userDtails.getUser().getEmail();
            User user = userService.findEmailByUser(email); // user 찾기
            Product product = productService.findProductNameUser(ProductId,user); // 유저와 상품명으로 상품 찾기

            productService.deleteProduct(product);

            ProductDTO deleteresponse = ProductDTO.builder()
                    .productId(product.getId())
                    .useremail(product.getUserId().getEmail())
                    .userId(product.getUserId().getUserId())
                    .userName(product.getUserId().getUsername())
                    .userPhoneNumber(product.getUserId().getPhoneNumber())
                    .title(product.getTitle())
                    .content(product.getContent())
                    .name(product.getName())
                    .price(product.getPrice())
                    .total(product.getTotal())
                    .imgUrl(product.getImgUrl())
                    .createDate(product.getCreateDate())
                    .build();

            return ResponseEntity.ok().body(deleteresponse);
        }
        catch (Exception e){
            ResponseDTO deleteresponse = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity.badRequest().body(deleteresponse);

        }

    }

    @GetMapping("/products")
    private ResponseEntity<?> findall(){
        List<Product> products = productService.findall();
        List<ProductDTO> productdtos = new ArrayList<>();
        for (Product product:products) {
            ProductDTO productDTO = ProductDTO.builder()
                    .productId(product.getId())
                    .useremail(product.getUserId().getEmail())
                    .userId(product.getUserId().getUserId())
                    .userName(product.getUserId().getUsername())
                    .userPhoneNumber(product.getUserId().getPhoneNumber())
                    .title(product.getTitle())
                    .content(product.getContent())
                    .name(product.getName())
                    .price(product.getPrice())
                    .total(product.getTotal())
                    .imgUrl(product.getImgUrl())
                    .createDate(product.getCreateDate())
                    .build();

            productdtos.add(productDTO);

        }
        Map<String , Object> result = new HashMap<>();
        result.put("data", productdtos);
        return ResponseEntity.ok().body(result);
    }
}
