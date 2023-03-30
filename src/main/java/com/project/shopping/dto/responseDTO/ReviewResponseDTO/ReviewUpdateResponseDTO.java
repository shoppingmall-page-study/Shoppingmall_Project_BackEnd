package com.project.shopping.dto.responseDTO.ReviewResponseDTO;

import com.project.shopping.model.Review;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ReviewUpdateResponseDTO {
    private int reviewId;
    private  String title;
    private  String content;
    private  String ImgUrl;

    @Builder

    public ReviewUpdateResponseDTO(int reviewId, String title, String content, String imgUrl) {
        this.reviewId = reviewId;
        this.title = title;
        this.content = content;
        this.ImgUrl = imgUrl;
    }

    public static ReviewUpdateResponseDTO toReviewUpdateResponseDTO(Review review){
        return ReviewUpdateResponseDTO.builder()
                .reviewId(review.getId())
                .title(review.getTitle())
                .content(review.getContent())
                .imgUrl(review.getImageUrl())
                .build();
    }



}
