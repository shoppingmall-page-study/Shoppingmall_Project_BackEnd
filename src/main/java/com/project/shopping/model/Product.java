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

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "User_ID")
    private User user;



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


    @Column(nullable = false)
    private  String status;


    @Builder
    private Product(User user, String title, String content, String name, long price, int total, String imgUrl, String status) {
        this.user = user;
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
    public void delete(){
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

}
