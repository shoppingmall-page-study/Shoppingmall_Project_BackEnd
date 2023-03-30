package com.project.shopping.dto.responseDTO.ReviewResponseDTO;

import com.project.shopping.model.Review;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ReviewCreateResponseDTO {
    private int reviewId;
    private  String title;
    private  String content;

    @Builder
    public ReviewCreateResponseDTO(int reviewId, String title, String content) {
        this.reviewId = reviewId;
        this.title = title;
        this.content = content;
    }


    public static  ReviewCreateResponseDTO toReviewCreateResponseDTO(Review review){
        return ReviewCreateResponseDTO.builder()
                .reviewId(review.getId())
                .title(review.getTitle())
                .content(review.getContent())
                .build();
    }

}
