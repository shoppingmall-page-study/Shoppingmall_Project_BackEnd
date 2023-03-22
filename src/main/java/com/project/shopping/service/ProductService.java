package com.project.shopping.service;


import com.project.shopping.Error.CustomException;
import com.project.shopping.Error.ErrorCode;
import com.project.shopping.dto.requestDTO.ProductRequestDTO.ProductCreateRequestDTO;
import com.project.shopping.dto.requestDTO.ProductRequestDTO.ProductUpdateRequestDTO;
import com.project.shopping.dto.responseDTO.ProductResponseDTO.ProductCreateResponseDTO;
import com.project.shopping.dto.responseDTO.ProductResponseDTO.ProductDeleteResponseDTO;
import com.project.shopping.dto.responseDTO.ProductResponseDTO.ProductJoinResponseDTO;
import com.project.shopping.dto.responseDTO.ProductResponseDTO.ProductSearchResponseDTO;
import com.project.shopping.model.Product;
import com.project.shopping.model.User;
import com.project.shopping.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public ProductCreateResponseDTO create(ProductCreateRequestDTO productCreateRequestDTO, User user){

        Product product = Product.builder().userId(user)
                .title(productCreateRequestDTO.getTitle())
                .content(productCreateRequestDTO.getContent())
                .name(productCreateRequestDTO.getName())
                .price(productCreateRequestDTO.getPrice())
                .total(productCreateRequestDTO.getTotal())
                .status("active")
                .imgUrl(productCreateRequestDTO.getImgUrl())
                .build(); // 상품 생성

        product = productRepository.save(product);

        ProductCreateResponseDTO productCreateResponseDTO = ProductCreateResponseDTO.builder()
                .title(product.getTitle())
                .content(product.getContent())
                .name(product.getName())
                .price(product.getPrice())
                .total(product.getTotal())
                .imgUrl(product.getImgUrl())
                .createDate(product.getCreateDate())
                .modifiedDate(product.getModifiedDate())
                .build();

        return productCreateResponseDTO;
    }


    public Product update(User user, ProductUpdateRequestDTO productUpdateRequestDTO, int ProductId){

        Product product = productRepository.findByIdAndUserId(ProductId, user)
                .orElseThrow(()-> new CustomException(ErrorCode.NotFoundProductException));// 해당 상품 찾기

        product.setTitle(productUpdateRequestDTO.getTitle());
        product.setContent(productUpdateRequestDTO.getContent());
        product.setName(productUpdateRequestDTO.getName());
        product.setPrice(productUpdateRequestDTO.getPrice());
        product.setTotal(productUpdateRequestDTO.getTotal());
        product.setImgUrl(productUpdateRequestDTO.getImgUrl());

        return productRepository.save(product);

    }
    public ProductJoinResponseDTO findById(int id){
        Product findProduct = productRepository.findById(id)
                .orElseThrow(()-> new CustomException(ErrorCode.NotFoundProductException));
        // dto
        ProductJoinResponseDTO productJoinResponseDTO = ProductJoinResponseDTO.builder()
                .productId(findProduct.getId())
                .title(findProduct.getTitle())
                .content(findProduct.getContent())
                .name(findProduct.getName())
                .price(findProduct.getPrice())
                .total(findProduct.getTotal())
                .imgUrl(findProduct.getImgUrl())
                .createDate(findProduct.getCreateDate())
                .modifiedDate(findProduct.getModifiedDate())
                .build();
        return  productJoinResponseDTO;

    }



    public ProductDeleteResponseDTO deleteProduct(User user, int ProductId){

        Product product = productRepository.findByIdAndUserId(ProductId,user)
                .orElseThrow(()-> new CustomException(ErrorCode.NotFoundProductException));// 유저와 상품명으로 상품 찾기
        product.setStatus("Disabled");
        productRepository.save(product);

        ProductDeleteResponseDTO productDeleteResponseDTO = ProductDeleteResponseDTO.builder()
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


        return productDeleteResponseDTO;
    }

    public List<ProductSearchResponseDTO> getProductList(String title, String status){

        List<Product> productList = productRepository.getProductList(title, status);
        List<ProductSearchResponseDTO> response  = new ArrayList<>();

        for(Product product : productList){
            ProductSearchResponseDTO productSearchResponseDTO = ProductSearchResponseDTO.builder()
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
            response.add(productSearchResponseDTO);
        }
        return response ;
    }



    public  List<ProductJoinResponseDTO> getActiveProdcutList(String status){
        System.out.println(status);
        List<Product> products = productRepository.getActiveProdcutList(status);
        if(products.size() == 0){
            throw new CustomException(ErrorCode.NotFoundProductException);
        }
        List<ProductJoinResponseDTO> productDtos = new ArrayList<>();
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

            productDtos.add(productProductsResponseDTO);

        }
        return productDtos;
    }

    public List<ProductJoinResponseDTO> getEqUserAndActive(User user, String status){

        List<Product> findAllProduct = productRepository.getEqUserAndActive(user, status);

        // dto 등록
        List<ProductJoinResponseDTO> response = new ArrayList<>();

        for(Product product: findAllProduct){
            ProductJoinResponseDTO productJoinResponseDTO = ProductJoinResponseDTO.builder()
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
            response.add(productJoinResponseDTO);
        }

        return response;
    }
}
