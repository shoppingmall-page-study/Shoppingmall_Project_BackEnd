package com.project.shopping.dto.responseDTO.ReviewResponseDTO;

import com.project.shopping.model.Review;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@AllArgsConstructor
public class ReviewUpdateResponseDTO {
    private int reviewId;
    private  String title;
    private  String content;
    private  String ImgUrl;


    public static ReviewUpdateResponseDTO toReviewUpdateResponseDTO(Review review){
        return new ReviewUpdateResponseDTO(review.getId(),review.getTitle(),review.getContent(),review.getImageUrl());
    }



}
