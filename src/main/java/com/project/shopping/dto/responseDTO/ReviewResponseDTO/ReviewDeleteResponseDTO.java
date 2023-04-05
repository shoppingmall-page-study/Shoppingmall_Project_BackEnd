package com.project.shopping.dto.responseDTO.ReviewResponseDTO;

import com.project.shopping.model.Review;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@AllArgsConstructor
public class ReviewDeleteResponseDTO {
    private int reviewId;
    private  String title;
    private  String content;

    public static ReviewDeleteResponseDTO toReviewDeleteResponseDTO(Review review){
        return new ReviewDeleteResponseDTO(review.getId(),review.getTitle(),review.getContent());
    }

}
