package com.project.shopping.model;

import com.project.shopping.dto.responseDTO.ReviewResponseDTO.*;
import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
public class Review extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="Review_ID")
    private int id;

    @ManyToOne
    @JoinColumn(name = "User_ID")
    private User userId;

    @ManyToOne
    @JoinColumn(name = "Product_ID")
    private Product productId;


    // set builder 패턴 이용해 바꾸기

    @Column(nullable = false, length=255)
    private String title;


    @Lob
    private String content;


    @Column(nullable = true, length=255)
    private String imageUrl;


    @Column(nullable = false)
    private  String status;



    @Builder
    private Review(User userId, Product productId, String title, String content, String imageUrl, String status) {
        this.userId = userId;
        this.productId = productId;
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
        this.status = status;
    }


    public void  update(final Review review){
        updateReviewContent(review.getContent());
        updateReviewTitle(review.getTitle());
        updateReviewImageUrl(review.getImageUrl());
    }
    public void delete(){
        this.status = "Disabled";
    }

    private void updateReviewImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    private void updateReviewTitle(String title) {
        this.title = title;
    }

    private void updateReviewContent(String content) {
        this.content = content;
    }


}
