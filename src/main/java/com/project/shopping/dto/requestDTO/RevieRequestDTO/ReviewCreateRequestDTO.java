package com.project.shopping.dto.requestDTO.RevieRequestDTO;

import com.project.shopping.model.Product;
import com.project.shopping.model.Review;
import com.project.shopping.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@Getter
public class ReviewCreateRequestDTO {
    //review

    @NotBlank
    private  String imgUrl;
    @NotBlank
    private  String title;
    @NotBlank
    private  String content;


    public Review toEntity(User user, Product product, String active){
        return Review.builder()
                .user(user)
                .product(product)
                .imageUrl(this.imgUrl)
                .title(this.title)
                .content(this.content)
                .status(active)
                .build();

    }
}
