package com.project.shopping.service;


import com.project.shopping.Error.CustomExcpetion;
import com.project.shopping.Error.ErrorCode;
import com.project.shopping.auth.PrincipalDetails;
import com.project.shopping.dto.requestDTO.ProductRequestDTO.ProductCreateRequestDTO;
import com.project.shopping.dto.requestDTO.ProductRequestDTO.ProductUpdateRequestDTO;
import com.project.shopping.model.Product;
import com.project.shopping.model.User;
import com.project.shopping.repository.ProductRepository;
import com.project.shopping.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;
    // 상품 생성
    public Product create(ProductCreateRequestDTO productCreateRequestDTO, Authentication authentication){

        PrincipalDetails userDtails = (PrincipalDetails) authentication.getPrincipal();
        String email = userDtails.getUser().getEmail();
        User user = userRepository.findByEmail(email); // 유저 찾기

        Product product = Product.builder().userId(user)
                .title(productCreateRequestDTO.getTitle())
                .content(productCreateRequestDTO.getContent())
                .name(productCreateRequestDTO.getName())
                .price(productCreateRequestDTO.getPrice())
                .total(productCreateRequestDTO.getTotal())
                .imgUrl(productCreateRequestDTO.getImgUrl())
                .status("active")
                .createDate(Timestamp.valueOf(LocalDateTime.now()))
                .modifiedDate(Timestamp.valueOf(LocalDateTime.now()))
                .build(); // 상품 생성

        System.out.println(product.getName());

        if(product == null  || product.getName() == "" ||  product.getTitle()== ""
                && product.getContent() == "" ||  product.getPrice() == 0 ||  product.getTotal() == 0
                ||  product.getImgUrl() == ""){
            throw  new CustomExcpetion("잘못된 형식의 데이터 입니다." , ErrorCode.BadParameterException);
        }

        return productRepository.save(product);
    }

    public Product update(Authentication authentication, ProductUpdateRequestDTO productUpdateRequestDTO, int ProductId){

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        String email = principalDetails.getUser().getEmail();
        User user = userRepository.findByEmail(email); // 유저 찾기
        Product product = productRepository.findByIdAndUserId(ProductId, user); // 해당 상품 찾기
        if(productUpdateRequestDTO.getTitle() != ""){
            product.setTitle(productUpdateRequestDTO.getTitle());
        }
        if(productUpdateRequestDTO.getContent() != ""){
            product.setContent(productUpdateRequestDTO.getContent());
        }
        if(productUpdateRequestDTO.getName() != ""){
            product.setName(productUpdateRequestDTO.getName());

        }
        if(productUpdateRequestDTO.getPrice() != 0){
            product.setPrice(productUpdateRequestDTO.getPrice());

        }
        if(productUpdateRequestDTO.getTotal() !=0){
            product.setTotal(productUpdateRequestDTO.getTotal());

        }
        if(productUpdateRequestDTO.getImgUrl() != ""){
            product.setImgUrl(productUpdateRequestDTO.getImgUrl());

        }
        product.setModifiedDate(Timestamp.valueOf(LocalDateTime.now()));


        return productRepository.save(product);

    }
    public Product findById(int id){return  productRepository.findById(id);}

    public Product findProductNameUser(int id, User user){
        return productRepository.findByIdAndUserId(id, user);
    }
    public Boolean existsPruductIdUser(int id , User user){return  productRepository.existsByIdAndUserId(id,user);}
    public Product deleteProduct(Authentication authentication, int ProductId){
        PrincipalDetails userDtails = (PrincipalDetails) authentication.getPrincipal();
        String email = userDtails.getUser().getEmail();

        User user = userRepository.findByEmail(email); // user 찾기
        if(!productRepository.existsByIdAndUserId(ProductId,user)){
            throw  new CustomExcpetion("상품이 존재하지 않습니다.",ErrorCode.NotFoundProductException);
        }
        Product product = productRepository.findByIdAndUserId(ProductId,user); // 유저와 상품명으로 상품 찾기
        product.setStatus("Disabled");
        productRepository.save(product);


        return product;
    }
    public Product findproductid(int id){
        return productRepository.findById(id);
    }

    public List<Product> findall(){
        return productRepository.findAll();
    }

    public List<Product> getProductList(String title, String stauts){return productRepository.getProductList(title, stauts);}

    public List<Product> findallByUserId(User user){return  productRepository.findAllByUserId(user); }

    public List<Product> getActiveProdcutList(String status){return  productRepository.getActiveProdcutList(status);}

    public List<Product> getEqUserAndActive(Authentication authentication, String status){
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        String email = principalDetails.getUser().getEmail();
        User user = userRepository.findByEmail(email); // 해당 유저 찾기
        return productRepository.getEqUserAndActive(user, status);
    }
}
