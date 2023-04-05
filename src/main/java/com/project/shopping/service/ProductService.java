package com.project.shopping.service;


import com.project.shopping.Error.CustomException;
import com.project.shopping.Error.ErrorCode;
import com.project.shopping.dto.requestDTO.ProductRequestDTO.ProductCreateRequestDTO;
import com.project.shopping.dto.requestDTO.ProductRequestDTO.ProductUpdateRequestDTO;
import com.project.shopping.dto.responseDTO.ProductResponseDTO.*;
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

        Product product =  productCreateRequestDTO.toEntity(user);
        product.activeState();

        productRepository.save(product);

        return ProductCreateResponseDTO.toProductCreateResponseDTO(product);
    }


    public ProductUpdateResponseDTO update(User user, ProductUpdateRequestDTO productUpdateRequestDTO, int ProductId){

        Product product = productRepository.findByIdAndUserId(ProductId, user)
                .orElseThrow(()-> new CustomException(ErrorCode.NotFoundProductException));// 해당 상품 찾기

        product.update(productUpdateRequestDTO.toEntity());
        productRepository.save(product);

        return ProductUpdateResponseDTO.toProductUpdateResponseDTO(product);

    }
    public ProductJoinResponseDTO findById(int id){
        Product findProduct = productRepository.findById(id)
                .orElseThrow(()-> new CustomException(ErrorCode.NotFoundProductException));
        // entitytoDTO
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
        product.delete();
        productRepository.save(product);



        return ProductDeleteResponseDTO.toProductDeleteResponseDTO(product);
    }

    public List<ProductSearchResponseDTO> getProductList(String title, String status){

        List<Product> productList = productRepository.getProductList(title, status);
        List<ProductSearchResponseDTO> response  = new ArrayList<>();

        for(Product product : productList){
            response.add(ProductSearchResponseDTO.toProductSearchResponseDTO(product));
        }
        return response ;
    }



    public  List<ProductJoinResponseDTO> getActiveProdcutList(String status){
        System.out.println(status);
        List<Product> products = productRepository.getActiveProdcutList(status);
        if(products.size() == 0){
            throw new CustomException(ErrorCode.NotFoundProductException);
        }
        List<ProductJoinResponseDTO> productJoinResponseDTOS = new ArrayList<>();
        for (Product product:products) {


            productJoinResponseDTOS.add(ProductJoinResponseDTO.toProductJoinResponseDTO(product));

        }
        return productJoinResponseDTOS;
    }

    public List<ProductJoinResponseDTO> getEqUserAndActive(User user, String status){

        List<Product> findAllProduct = productRepository.getEqUserAndActive(user, status);

        // dto 등록
        List<ProductJoinResponseDTO> productJoinResponseDTOS = new ArrayList<>();

        for(Product product: findAllProduct){
            productJoinResponseDTOS.add(ProductJoinResponseDTO.toProductJoinResponseDTO(product));
        }

        return productJoinResponseDTOS;
    }
}
