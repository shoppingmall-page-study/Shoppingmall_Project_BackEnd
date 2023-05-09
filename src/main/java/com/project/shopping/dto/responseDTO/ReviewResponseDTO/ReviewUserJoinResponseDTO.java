package com.project.shopping.dto.responseDTO.ReviewResponseDTO;

import com.project.shopping.model.Review;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class ReviewUserJoinResponseDTO {
    private int reviewId;
    private  String title;

    private  String content;

    private LocalDateTime createDate;
    private LocalDateTime modifiedDate;
    private  String imgUrl;

    @Builder
    private ReviewUserJoinResponseDTO(int reviewId, String title, String content, LocalDateTime createDate, LocalDateTime modifiedDate, String imgUrl) {
        this.reviewId = reviewId;
        this.title = title;
        this.content = content;
        this.createDate = createDate;
        this.modifiedDate = modifiedDate;
        this.imgUrl = imgUrl;
    }



    public static ReviewUserJoinResponseDTO toReviewUserJoinResponseDTO(Review review){
        return ReviewUserJoinResponseDTO.builder()
                .reviewId(review.getId())
                .content(review.getContent())
                .title(review.getTitle())
                .createDate(review.getCreateDate())
                .modifiedDate(review.getModifiedDate())
                .imgUrl(review.getImageUrl())
                .build();
    }

}
