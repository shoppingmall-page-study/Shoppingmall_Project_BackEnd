package com.project.shopping.dto.responseDTO.ReviewResponseDTO;

import com.project.shopping.model.Review;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ReviewCreateResponseDTO {
    private int reviewId;
    private  String title;
    private  String content;

    private ReviewCreateResponseDTO(int reviewId, String title, String content) {
        this.reviewId = reviewId;
        this.title = title;
        this.content = content;
    }

    public static  ReviewCreateResponseDTO toReviewCreateResponseDTO(Review review){
        return new ReviewCreateResponseDTO(review.getId(),review.getTitle(),review.getContent());
    }

}
