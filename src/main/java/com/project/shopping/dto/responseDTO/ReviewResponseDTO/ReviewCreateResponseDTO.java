package com.project.shopping.dto.responseDTO.ReviewResponseDTO;

import com.project.shopping.model.Review;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@AllArgsConstructor
public class ReviewCreateResponseDTO {
    private int reviewId;
    private  String title;
    private  String content;



    public static  ReviewCreateResponseDTO toReviewCreateResponseDTO(Review review){
        return new ReviewCreateResponseDTO(review.getId(),review.getTitle(),review.getContent());
    }

}
