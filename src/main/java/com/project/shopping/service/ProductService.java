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


    public ProductUpdateResponseDTO update(User user, ProductUpdateRequestDTO productUpdateRequestDTO, int product){

        Product findProduct = productRepository.findByIdAndUser(product, user)
                .orElseThrow(()-> new CustomException(ErrorCode.NotFoundProductException));// 해당 상품 찾기

        findProduct.update(productUpdateRequestDTO.toEntity());
        productRepository.save(findProduct);

        return ProductUpdateResponseDTO.toProductUpdateResponseDTO(findProduct);

    }
    public ProductJoinResponseDTO findById(int id){
        Product findProduct = productRepository.findById(id)
                .orElseThrow(()-> new CustomException(ErrorCode.NotFoundProductException));
        // entitytoDTO
        return ProductJoinResponseDTO.toProductJoinResponseDTO(findProduct);

    }



    public ProductDeleteResponseDTO deleteProduct(User user, int product){

        Product findProduct = productRepository.findByIdAndUser(product,user)
                .orElseThrow(()-> new CustomException(ErrorCode.NotFoundProductException));// 유저와 상품명으로 상품 찾기
        findProduct.delete();
        productRepository.save(findProduct);



        return ProductDeleteResponseDTO.toProductDeleteResponseDTO(findProduct);
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
        List<Product> products = productRepository.getActiveProdcutList(status);
        if(products.isEmpty()){
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
