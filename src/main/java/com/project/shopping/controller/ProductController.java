package com.project.shopping.controller;


import com.project.shopping.Error.CustomExcpetion;
import com.project.shopping.Error.ErrorCode;
import com.project.shopping.dto.requestDTO.ProductRequestDTO.ProductCreateRequestDTO;
import com.project.shopping.dto.requestDTO.ProductRequestDTO.ProductSearchRequestDTO;
import com.project.shopping.dto.requestDTO.ProductRequestDTO.ProductUpdateRequestDTO;
import com.project.shopping.dto.responseDTO.ProductResponseDTO.*;
import com.project.shopping.model.Product;
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

import java.io.IOException;
import java.util.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ProductController {


    private final  ProductService productService;

    private String ActiveStatus= "active";

    @Value("${file.dir}")
    private String fileDir;


//    @PostMapping("/api/product/create")
//    public ResponseEntity<?> createProduct(Authentication authentication, @RequestBody ProductCreateRequestDTO productCreateRequestDTO) {
//        if(authentication == null){
//            throw  new CustomExcpetion("허용되지 않은 접근입니다." , ErrorCode.UnauthorizedException);
//        }
//
//        ProductCreateResponseDTO registeredProduct = productService.create(productCreateRequestDTO,authentication); // 상품 생성
//
//        Map<String, Object> result = new HashMap<>();
//        result.put("msg", "상품 등록에 성공했습니다.");
//        result.put("data", registeredProduct);
//        return ResponseEntity.ok().body(result);
//    }

    @PostMapping("/api/product/create")
    public ResponseEntity<?> createProduct(Authentication authentication, @RequestPart(required = false) MultipartFile img, @RequestPart ProductCreateRequestDTO productCreateRequestDTO) throws IOException {
        if(authentication == null){
            throw  new CustomExcpetion("허용되지 않은 접근입니다." , ErrorCode.UnauthorizedException);
        }
        System.out.println(productCreateRequestDTO.getContent());
        ProductCreateResponseDTO responseDTO = productService.create(authentication, img, productCreateRequestDTO);
        Map<String, Object> result = new HashMap<>();
        result.put("msg", "상품 등록에 성공했습니다.");
        result.put("data", responseDTO);
        return ResponseEntity.ok().body(result);

    }


//    private  final FileRepository fileRepository;
//    @PostMapping(value = "/info")
//    public Resource bookshelfInfo(@RequestPart(required = false) MultipartFile thumbnail,
//                                  @RequestPart request request) throws IOException {
//
//
//            log.info("file name = "+thumbnail.getOriginalFilename());
//            log.info("dto info = "+request.getInfo());
//
//            String origName = thumbnail.getOriginalFilename();
//            String uuid = UUID.randomUUID().toString();
//            String extension = origName.substring(origName.lastIndexOf("."));
//
//            String savedName  = uuid +extension;
//            String savedPath =fileDir + savedName;
//
//            FileEntity file = FileEntity.builder()
//                    .orgNm(origName)
//                    .savedNm(savedName)
//                    .savedPath(savedPath)
//                    .build();
//            thumbnail.transferTo(new File(savedPath));
//            fileRepository.save(file);
//
//
//            return new UrlResource("file:" + file.getSavedPath());
//
//
//    }

    @GetMapping(
            value = "/api/view/{fileId}",
            produces = MediaType.IMAGE_JPEG_VALUE
    )
    public Resource downloadImage(@PathVariable("fileId") int id) throws IOException{

        Product product = productService.findproduct(id);
        //FileEntity file = fileRepository.findById(id).orElse(null);
        return new UrlResource("file:" + product.getImgUrl());
    }



    //
    @DeleteMapping("/api/product/delete/{id}")
    public ResponseEntity<?>  productdelete(Authentication authentication, @PathVariable(value = "id") int ProductId){

        if(authentication == null){
            throw  new CustomExcpetion("허용되지 않은 접근입니다." , ErrorCode.UnauthorizedException);
        }

        ProductDeleteResponseDTO product = productService.deleteProduct(authentication, ProductId);


        Map<String, Object> result = new HashMap<>();
        result.put("msg", "상품삭제에 성공했습니다.");
        result.put("data", product);

        return ResponseEntity.ok().body(result);

    }


    private  final ProductRepository productRepository;

    @GetMapping("/api/products")
    private ResponseEntity<?> findall(){
        //List<ProductJoinResponseDTO> productdtos = productService.getActiveProdcutList(ActiveStatus);
        // repository 에서 값을 못 꺼네온다
        List<Product> products = productRepository.findAll();
        if(products.size() == 0){
            System.out.println("상품 이 없습니다.");
            throw  new CustomExcpetion("상품이 존재하지 않습니다.",ErrorCode.NotFoundProductException);
        }
        List<ProductJoinResponseDTO> productdtoss = new ArrayList<>();
        for (Product product:products) {
            ProductJoinResponseDTO productProductsResponseDTO = ProductJoinResponseDTO.builder()
                    .productId(product.getId())
                    .title(product.getTitle())
                    .content(product.getContent())
                    .name(product.getName())
                    .price(product.getPrice())
                    .total(product.getTotal())
                    .imgUrl(product.getImgUrl())
                    .createDate(product.getCreateDate())
                    .modifiedDate(product.getModifiedDate())
                    .build();

            productdtoss.add(productProductsResponseDTO);

        }

        Map<String , Object> result = new HashMap<>();
        result.put("msg","상품 조회에 성공했습니다.");
        result.put("data", productdtoss);
        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/api/product/search")
    public ResponseEntity<?> searchProudct(@RequestBody ProductSearchRequestDTO productSearchRequestDTO){
        List<ProductSearchResponseDTO> response = productService.getProductList(productSearchRequestDTO.getKeyword(), ActiveStatus);

        Map<String , Object> result = new HashMap<>();
        result.put("msg","상품검색에 성공했습니다.");
        result.put("data",response);
        return ResponseEntity.ok().body(result);

    }
    // 내가 올린 상품 검색
    @GetMapping("/api/products/user")
    public ResponseEntity<?> findresisterproductuser(Authentication authentication){
        if(authentication == null){
            throw  new CustomExcpetion("허용되지 않은 접근입니다." , ErrorCode.UnauthorizedException);
        }

        List<ProductJoinResponseDTO> response = productService.getEqUserAndActive(authentication, ActiveStatus); // 해당 유저가 등록한 상품들 찾기


        Map<String , Object> result = new HashMap<>();
        result.put("msg","상품 조회에 성공했습니다.");
        result.put("data",response);
        return ResponseEntity.ok().body(result);

    }

    @GetMapping("/api/product/{id}")
    public ResponseEntity<?>  productfind(@PathVariable(value = "id") int ProductId){
        //service
        ProductJoinResponseDTO productJoinResponseDTO = productService.findById(ProductId);


        Map<String, Object> result = new HashMap<>();
        result.put("msg","상품검색에 성공했습니다.");
        result.put("data", productJoinResponseDTO);

        return ResponseEntity.ok().body(result);

    }

    @PutMapping("/api/product/update/{id}")
    public ResponseEntity<?> updateProduct(Authentication authentication, @PathVariable(value = "id") int ProductId, @RequestBody ProductUpdateRequestDTO productUpdateRequestDTO){
        if(authentication == null){
            throw  new CustomExcpetion("허용되지 않은 접근입니다." , ErrorCode.UnauthorizedException);
        }

        Product product = productService.update(authentication, productUpdateRequestDTO, ProductId);

        ProductUpdateResponseDTO productUpdateResponseDTO = ProductUpdateResponseDTO.builder()
                .productId(product.getId())
                .title(product.getTitle())
                .content(product.getContent())
                .name(product.getName())
                .price(product.getPrice())
                .total(product.getTotal())
                .imgUrl(product.getImgUrl())
                .createDate(product.getModifiedDate())
                .modifiedDate(product.getModifiedDate())
                .build();

        Map<String , Object> result = new HashMap<>();
        result.put("msg","상품 수정에 성공했습니다.");
        result.put("data",productUpdateResponseDTO);

        return ResponseEntity.ok().body(result);
    }




}
