package com.project.shopping.model;

import com.project.shopping.dto.responseDTO.ProductResponseDTO.*;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
public class Product extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="Product_ID")
    private int id;

    @ManyToOne
    @JoinColumn(name = "User_ID")
    private User userId;



    @Column(nullable = false)
    private String title;


    @Lob
    private String content;


    @Column(nullable = false)
    private String name;


    @Column(nullable = false)
    private long price;


    @Column(nullable = false)
    private int total;


    @Column(nullable = false)
    private String imgUrl;


    @OneToMany(mappedBy = "productId")
    private List<Review> reviews = new ArrayList<>(); // 일대 다 review와 연관 관계 맺기


    @OneToMany(mappedBy = "productId")
    private  List<Cart> carts = new ArrayList<>();



    @Column(nullable = false)
    private  String status;


    @Builder
    private Product(User userId, String title, String content, String name, long price, int total, String imgUrl, String status) {
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.name = name;
        this.price = price;
        this.total = total;
        this.imgUrl = imgUrl;
        this.status = status;
    }

    public void update(final  Product product){
        updateProductTitle(product.getTitle());
        updateProductContent(product.getContent());
        updateProductName(product.getName());
        updateProductPrice(product.getPrice());
        updateProductTotal(product.getTotal());
        updateProductImgUrl(product.getImgUrl());

    }
    public void disabledState(){
        this.status = "Disabled";
    }
    public void activeState() {this.status = "active";}


    private void updateProductImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    private void updateProductTotal(int total) {
        this.total = total;


    }

    private void updateProductPrice(long price) {
        this.price = price;

    }

    private void updateProductName(String name) {
        this.name = name;

    }

    private void updateProductContent(String content) {
        this.content = content;

    }

    private void updateProductTitle(String title) {
        this.title = title;
    }


    // entity -> dto
    public ProductCreateResponseDTO toProductCreateResponseDTO(){
        return ProductCreateResponseDTO.builder()
                .title(this.title)
                .content(this.content)
                .name(this.name)
                .price(this.price)
                .total(this.total)
                .imgUrl(this.imgUrl)
                .createDate(this.getCreateDate())
                .modifiedDate(this.getModifiedDate())
                .build();
    }
    public ProductDeleteResponseDTO toProductDeleteResponseDTO(){
        return ProductDeleteResponseDTO.builder()
                .productId(this.id)
                .title(this.title)
                .content(this.content)
                .name(this.name)
                .price(this.price)
                .total(this.total)
                .imgUrl(this.imgUrl)
                .createDate(this.getCreateDate())
                .modifiedDate(this.getModifiedDate())
                .build();

    }

    public  ProductJoinResponseDTO toProductJoinResponseDTO(){
        return ProductJoinResponseDTO.builder()
                .productId(this.id)
                .title(this.title)
                .content(this.content)
                .name(this.name)
                .price(this.price)
                .total(this.total)
                .imgUrl(this.imgUrl)
                .createDate(this.getCreateDate())
                .modifiedDate(this.getModifiedDate())
                .build();
    }

    public ProductSearchResponseDTO toProductSearchResponseDTO(){
        return ProductSearchResponseDTO.builder()
                .productId(this.id)
                .title(this.title)
                .content(this.content)
                .name(this.name)
                .price(this.price)
                .total(this.total)
                .imgUrl(this.imgUrl)
                .createDate(this.getCreateDate())
                .modifiedDate(this.getModifiedDate())
                .build();
    }

    public ProductUpdateResponseDTO toProductUpdateResponseDTO(){
        return ProductUpdateResponseDTO.builder()
                .productId(this.id)
                .title(this.title)
                .content(this.content)
                .name(this.name)
                .price(this.price)
                .total(this.total)
                .imgUrl(this.imgUrl)
                .createDate(this.getCreateDate())
                .modifiedDate(this.getModifiedDate())
                .build();
    }


}
