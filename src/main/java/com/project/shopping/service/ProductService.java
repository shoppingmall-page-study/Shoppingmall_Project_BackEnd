package com.project.shopping.service;


import com.project.shopping.Error.CustomExcpetion;
import com.project.shopping.Error.ErrorCode;
import com.project.shopping.auth.PrincipalDetails;
import com.project.shopping.dto.requestDTO.ProductRequestDTO.ProductCreateRequestDTO;
import com.project.shopping.dto.requestDTO.ProductRequestDTO.ProductUpdateRequestDTO;
import com.project.shopping.dto.responseDTO.ProductResponseDTO.ProductCreateResponseDTO;
import com.project.shopping.dto.responseDTO.ProductResponseDTO.ProductDeleteResponseDTO;
import com.project.shopping.dto.responseDTO.ProductResponseDTO.ProductJoinResponseDTO;
import com.project.shopping.dto.responseDTO.ProductResponseDTO.ProductSearchResponseDTO;
import com.project.shopping.model.Product;
import com.project.shopping.model.User;
import com.project.shopping.repository.ProductRepository;
import com.project.shopping.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {


    private final ProductRepository productRepository;


    private final  UserRepository userRepository;
    // 상품 생성
//    public ProductCreateResponseDTO create(ProductCreateRequestDTO productCreateRequestDTO, Authentication authentication){
//
//        PrincipalDetails userDtails = (PrincipalDetails) authentication.getPrincipal();
//        String email = userDtails.getUser().getEmail();
//        User user = userRepository.findByEmail(email); // 유저 찾기
//
//        Product product = Product.builder().userId(user)
//                .title(productCreateRequestDTO.getTitle())
//                .content(productCreateRequestDTO.getContent())
//                .name(productCreateRequestDTO.getName())
//                .price(productCreateRequestDTO.getPrice())
//                .total(productCreateRequestDTO.getTotal())
//                .status("active")
//                .createDate(Timestamp.valueOf(LocalDateTime.now()))
//                .modifiedDate(Timestamp.valueOf(LocalDateTime.now()))
//                .build(); // 상품 생성
//
//        System.out.println(product.getName());
//
//        if(product == null  || product.getName() == "" ||  product.getTitle()== ""
//                && product.getContent() == "" ||  product.getPrice() == 0 ||  product.getTotal() == 0
//                ||  product.getImgUrl() == ""){
//            throw  new CustomExcpetion("잘못된 형식의 데이터 입니다." , ErrorCode.BadParameterException);
//        }
//        productRepository.save(product);
//        ProductCreateResponseDTO productCreateResponseDTO = ProductCreateResponseDTO.builder()
//                .title(product.getTitle())
//                .content(product.getContent())
//                .name(product.getName())
//                .price(product.getPrice())
//                .total(product.getTotal())
//                .imgUrl(product.getImgUrl())
//                .createDate(product.getCreateDate())
//                .modifiedDate(product.getModifiedDate())
//                .build();
//
//        return productCreateResponseDTO;
//    }
    @Value("${file.dir}")
    private String fileDir;


    public ProductCreateResponseDTO create(Authentication authentication, MultipartFile img, ProductCreateRequestDTO productCreateRequestDTO) throws IOException {

        PrincipalDetails userDtails = (PrincipalDetails) authentication.getPrincipal();
        String email = userDtails.getUser().getEmail();
        User user = userRepository.findByEmail(email); // 유저 찾기
        System.out.println(productCreateRequestDTO.getContent());

        File Folder = new File(fileDir);
        if(!Folder.exists()){
            try{
                Folder.mkdir();
            }catch (Exception e){
                e.getStackTrace();
            }
        }

        // 파일 생성 및 저장
        String origName = img.getOriginalFilename();
        String uuid = UUID.randomUUID().toString();
        String extension = origName.substring(origName.lastIndexOf("."));
        String savedName  = uuid +extension;
        String savedPath =fileDir + savedName;
        img.transferTo(new File(savedPath));

        Product product = Product.builder().userId(user)
                .title(productCreateRequestDTO.getTitle())
                .content(productCreateRequestDTO.getContent())
                .name(productCreateRequestDTO.getName())
                .price(productCreateRequestDTO.getPrice())
                .total(productCreateRequestDTO.getTotal())
                .status("active")
                .imgUrl(savedPath)
                .createDate(Timestamp.valueOf(LocalDateTime.now()))
                .modifiedDate(Timestamp.valueOf(LocalDateTime.now()))
                .build(); // 상품 생성

        System.out.println(product.getName());

        if(product == null  || product.getName() == "" ||  product.getTitle()== ""
                && product.getContent() == "" ||  product.getPrice() == 0 ||  product.getTotal() == 0
                ||  product.getImgUrl() == ""){
            throw  new CustomExcpetion("잘못된 형식의 데이터 입니다." , ErrorCode.BadParameterException);
        }
        productRepository.save(product);
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
    public ProductJoinResponseDTO findById(int id){
        Product findproduct = productRepository.findById(id);
        // dto
        ProductJoinResponseDTO productJoinResponseDTO = ProductJoinResponseDTO.builder()
                .productId(findproduct.getId())
                .title(findproduct.getTitle())
                .content(findproduct.getContent())
                .name(findproduct.getName())
                .price(findproduct.getPrice())
                .total(findproduct.getTotal())
                .imgUrl(findproduct.getImgUrl())
                .createDate(findproduct.getCreateDate())
                .modifiedDate(findproduct.getModifiedDate())
                .build();
        return  productJoinResponseDTO;

    }
    public Product findproduct(int id){
        Product findProduct = productRepository.findById(id);

        return findProduct;
    }

    public Product findProductNameUser(int id, User user){
        return productRepository.findByIdAndUserId(id, user);
    }
    public Boolean existsPruductIdUser(int id , User user){return  productRepository.existsByIdAndUserId(id,user);}
    public ProductDeleteResponseDTO deleteProduct(Authentication authentication, int ProductId){
        PrincipalDetails userDtails = (PrincipalDetails) authentication.getPrincipal();
        String email = userDtails.getUser().getEmail();

        User user = userRepository.findByEmail(email); // user 찾기
        if(!productRepository.existsByIdAndUserId(ProductId,user)){
            throw  new CustomExcpetion("상품이 존재하지 않습니다.",ErrorCode.NotFoundProductException);
        }
        Product product = productRepository.findByIdAndUserId(ProductId,user); // 유저와 상품명으로 상품 찾기
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
    public Product findproductid(int id){
        return productRepository.findById(id);
    }

    public List<Product> findall(){
        return productRepository.findAll();
    }

    public List<ProductSearchResponseDTO> getProductList(String title, String stauts){

        List<Product> productList = productRepository.getProductList(title, stauts);
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

    public List<Product> findallByUserId(User user){return  productRepository.findAllByUserId(user); }

    public  List<ProductJoinResponseDTO> getActiveProdcutList(String status){
        List<Product> products = productRepository.getActiveProdcutList(status);
        List<ProductJoinResponseDTO> productdtos = new ArrayList<>();
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

            productdtos.add(productProductsResponseDTO);

        }
        return productdtos;
    }

    public List<ProductJoinResponseDTO> getEqUserAndActive(Authentication authentication, String status){
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        String email = principalDetails.getUser().getEmail();
        User user = userRepository.findByEmail(email); // 해당 유저 찾기


        List<Product> findallproduct = productRepository.getEqUserAndActive(user, status);

        // dto 등록
        List<ProductJoinResponseDTO> response = new ArrayList<>();

        for(Product product: findallproduct){
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
