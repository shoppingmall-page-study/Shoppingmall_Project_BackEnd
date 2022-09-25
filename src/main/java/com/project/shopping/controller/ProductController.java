package com.project.shopping.controller;


import com.project.shopping.auth.PrincipalDetails;
import com.project.shopping.dto.ProductDTO;
import com.project.shopping.dto.ResponseDTO;
import com.project.shopping.dto.SearchDTO;
import com.project.shopping.model.Cart;
import com.project.shopping.model.Product;
import com.project.shopping.model.Review;
import com.project.shopping.model.User;
import com.project.shopping.service.CartService;
import com.project.shopping.service.ProductService;
import com.project.shopping.service.ReviewService;
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

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private CartService cartService;

    @PostMapping("/product/create")
    public ResponseEntity<?> createProduct(Authentication authentication, @RequestBody  ProductDTO productDTO){
//        System.out.println(productDTO.getTitle());
//        System.out.println(productDTO.getName());
//        System.out.println(productDTO.getPrice());
//        System.out.println(productDTO.getTotal());
//        System.out.println(productDTO.getImgUrl());
        PrincipalDetails userDtails = (PrincipalDetails) authentication.getPrincipal();
        String email = userDtails.getUser().getEmail();
        User user = userService.findEmailByUser(email);


        // 인증 값 기반으로 user 찾기

        Product product = Product.builder().userId(user)
                .title(productDTO.getTitle())
                .content(productDTO.getContent())
                .name(productDTO.getName())
                .amount(productDTO.getAmount())
                .total(productDTO.getTotal())
                .imgUrl(productDTO.getImgUrl())
                .status("active")
                .createDate(Timestamp.valueOf(LocalDateTime.now())).build();
        //System.out.println("12313213");
        // System.out.println(productDTO.getName());
        // System.out.println(product.getName());
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
                .amount(registeredProduct.getAmount())
                .total(registeredProduct.getTotal())
                .imgUrl(registeredProduct.getImgUrl())
                .createDate(registeredProduct.getCreateDate())
                .build();
        return ResponseEntity.ok().body(response);

//        try{
//
//        }catch (Exception e){
//            ResponseDTO response = ResponseDTO.builder().error(e.getMessage()).build();
//            System.out.println(e.getMessage());
//            return ResponseEntity.badRequest().body(response);
//        }

    }


    // 이미지
//    @GetMapping("/p")
//    public void imagepr(HttpServletRequest req){
//        String webPath = "resources/images/imtes/";
//        String folderPath = req.getSession().getServletContext().getRealPath(webPath);
//
//        System.out.println(folderPath);
//
//
//    }

//    @PostMapping(value = "/product/create", consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE })
//    public ResponseEntity<?> upload(Authentication authentication, @RequestPart MultipartFile file, @RequestPart ProductRequestDTO requestDTO, HttpServletRequest request) {
//        String originalFileName = file.getOriginalFilename();
//        LocalDateTime now = LocalDateTime.now();
//        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
//        String current_date = now.format(dateTimeFormatter);
//        String realPath = request.getSession().getServletContext().getRealPath("/");
//        File destination = new File(realPath);
//        if(! destination.exists()){
//            destination.mkdir();
//        }
//        String path = realPath + current_date+ originalFileName ;
//        File tempFile = null;
//        try {
//            PrincipalDetails userDtails = (PrincipalDetails) authentication.getPrincipal();
//            String email = userDtails.getUser().getEmail();
//            User user = userService.findEmailByUser(email);
//            tempFile = new File(path);
//            file.transferTo(tempFile);
//            System.out.println(path);
//            Product product = Product.builder().userId(user)
//                    .title(requestDTO.getTitle())
//                    .content(requestDTO.getContent())
//                    .name(requestDTO.getName())
//                    .price(requestDTO.getPrice())
//                    .total(requestDTO.getTotal())
//                    .imgUrl(path)
//                    .createDate(Timestamp.valueOf(LocalDateTime.now())).build();
//            Product registeredProduct = productService.create(product); // 상품 생성
//            //System.out.println(registeredProduct.getImgUrl());
//
//            ProductDTO response = ProductDTO.builder()
//                    .productId(registeredProduct.getId())
//                    .useremail(registeredProduct.getUserId().getEmail())
//                    .userId(registeredProduct.getUserId().getUserId())
//                    .userName(registeredProduct.getUserId().getUsername())
//                    .userPhoneNumber(registeredProduct.getUserId().getPhoneNumber())
//                    .title(registeredProduct.getTitle())
//                    .content(registeredProduct.getContent())
//                    .name(registeredProduct.getName())
//                    .price(registeredProduct.getPrice())
//                    .total(registeredProduct.getTotal())
//                    .imgUrl(registeredProduct.getImgUrl())
//                    .createDate(registeredProduct.getCreateDate())
//                    .build();
//            return ResponseEntity.ok().body(response);
//        } catch (IOException e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(originalFileName);
//        }
//
//    }
//
    @DeleteMapping("/product/delete/{id}")
    public ResponseEntity<?>  productdelete(Authentication authentication, @PathVariable(value = "id") int ProductId){


        try{
            PrincipalDetails userDtails = (PrincipalDetails) authentication.getPrincipal();
            String email = userDtails.getUser().getEmail();
            User user = userService.findEmailByUser(email); // user 찾기
            Product product = productService.findProductNameUser(ProductId,user); // 유저와 상품명으로 상품 찾기
            // 해당 상품을 찾았으니까
            // 해당 상품내 있는 리뷰 리스트
            List<Review> reviews = product.getReviews();

            List<Cart> carts = product.getCarts();

            for(Review review : reviews){
                reviewService.deleteReview(review);
            } // db 해당 리뷰 삭제

            for(Cart cart : carts){
                cartService.deleteCart(cart);
            } // db 해당 카트 삭제


            reviews.clear(); // 리뷰 전체 초기화 후
            carts.clear(); // 카트 전체 초기화 후

            productService.deleteProduct(product); // 상품 삭제

            ProductDTO deleteresponse = ProductDTO.builder()
                    .productId(product.getId())
                    .useremail(product.getUserId().getEmail())
                    .userId(product.getUserId().getUserId())
                    .userName(product.getUserId().getUsername())
                    .userPhoneNumber(product.getUserId().getPhoneNumber())
                    .title(product.getTitle())
                    .content(product.getContent())
                    .name(product.getName())
                    .amount(product.getAmount())
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
                    .amount(product.getAmount())
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

    @PostMapping("/product/search")
    public ResponseEntity<?> searchProudct(@RequestBody SearchDTO searchDTO){
        try{
            List<Product> productList = productService.getProductList(searchDTO.getSearchparam());
            List<ProductDTO> response  = new ArrayList<>();

            for(Product product : productList){
                ProductDTO productDTO = ProductDTO.builder()
                        .productId(product.getId())
                        .useremail(product.getUserId().getEmail())
                        .userId(product.getUserId().getUserId())
                        .userName(product.getUserId().getUsername())
                        .userPhoneNumber(product.getUserId().getPhoneNumber())
                        .title(product.getTitle())
                        .content(product.getContent())
                        .name(product.getName())
                        .amount(product.getAmount())
                        .total(product.getTotal())
                        .imgUrl(product.getImgUrl())
                        .createDate(product.getCreateDate())
                        .build();
                response.add(productDTO);
            }
            Map<String , Object> result = new HashMap<>();
            result.put("data",response);
            result.put("length",response.size());
            return ResponseEntity.ok().body(result);
        }catch (Exception e)
        {
            return  ResponseEntity.badRequest().body(e.getMessage());
        }

    }
    // 내가 올린 상품 검색
    @GetMapping("/product/list/user")
    public ResponseEntity<?> findresisterproductuser(Authentication authentication){
        try{
            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
            String email = principalDetails.getUser().getEmail();
            User user = userService.findEmailByUser(email); // 해당 유저 찾기

            List<Product> findallproduct = productService.findallByUserId(user); // 해당 유저가 등록한 상품들 찾기

            List<ProductDTO> response = new ArrayList<>();

            for(Product product: findallproduct){
                ProductDTO productDTO = ProductDTO.builder()
                        .productId(product.getId())
                        .useremail(product.getUserId().getEmail())
                        .userId(product.getUserId().getUserId())
                        .userName(product.getUserId().getUsername())
                        .userPhoneNumber(product.getUserId().getPhoneNumber())
                        .title(product.getTitle())
                        .content(product.getContent())
                        .name(product.getName())
                        .amount(product.getAmount())
                        .total(product.getTotal())
                        .imgUrl(product.getImgUrl())
                        .createDate(product.getCreateDate())
                        .build();
                response.add(productDTO);
            }
            Map<String , Object> result = new HashMap<>();
            result.put("data",response);
            return ResponseEntity.ok().body(result);

        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @PutMapping("/product/update/{id}")
    public ResponseEntity<?> updateProduct(Authentication authentication, @PathVariable(value = "id") int ProductId, @RequestBody ProductDTO productDTO){
        try{
            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
            String email = principalDetails.getUser().getEmail();
            User user = userService.findEmailByUser(email); // 유저 찾기
            Product product = productService.findProductNameUser(ProductId, user); // 해당 상품 찾기

            if(productDTO.getTitle() != ""){
                product.setTitle(productDTO.getTitle());
            }
            if(productDTO.getContent() != ""){
                product.setContent(productDTO.getContent());
            }
            if(productDTO.getName() != ""){
                product.setName(productDTO.getName());

            }
            if(productDTO.getAmount() != 0){
                product.setAmount(productDTO.getAmount());

            }
            if(productDTO.getTotal() !=0){
                product.setTotal(productDTO.getTotal());

            }
            if(productDTO.getImgUrl() != ""){
                product.setImgUrl(productDTO.getImgUrl());

            }
            product.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));



            productService.create(product);

            ProductDTO response = ProductDTO.builder()
                    .productId(product.getId())
                    .useremail(product.getUserId().getEmail())
                    .userId(product.getUserId().getUserId())
                    .userName(product.getUserId().getUsername())
                    .userPhoneNumber(product.getUserId().getPhoneNumber())
                    .title(product.getTitle())
                    .content(product.getContent())
                    .name(product.getName())
                    .amount(product.getAmount())
                    .total(product.getTotal())
                    .imgUrl(product.getImgUrl())
                    .createDate(product.getCreateDate())
                    .build();

            return ResponseEntity.ok().body(response);




        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }




}
