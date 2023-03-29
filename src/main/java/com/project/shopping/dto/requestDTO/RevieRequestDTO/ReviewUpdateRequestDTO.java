package com.project.shopping.dto.requestDTO.RevieRequestDTO;

import com.project.shopping.model.Product;
import com.project.shopping.model.Review;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@Getter
public class ReviewUpdateRequestDTO {
    @NotBlank
    private  String imgUrl;
    @NotBlank
    private  String title;
    @NotBlank
    private  String content;


    public Review toEntity(){
        return Review.builder()
                .imageUrl(this.imgUrl)
                .title(this.title)
                .content(this.content)
                .build();
    }
}
