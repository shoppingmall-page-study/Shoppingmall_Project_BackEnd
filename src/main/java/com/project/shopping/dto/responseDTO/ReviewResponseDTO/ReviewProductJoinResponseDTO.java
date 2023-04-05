package com.project.shopping.dto.responseDTO.ReviewResponseDTO;

import com.project.shopping.model.Review;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class ReviewProductJoinResponseDTO {
    private int reviewId;
    private  String title;
    private  String content;

    private LocalDateTime createDate;
    private LocalDateTime modifiedDate;
    private  String userNickname;
    private  String ImgURL;

    @Builder

    public ReviewProductJoinResponseDTO(int reviewId, String title, String content, LocalDateTime createDate,String userNickname,String imgURL, LocalDateTime modifiedDate) {
        this.reviewId = reviewId;
        this.title = title;
        this.content = content;
        this.userNickname = userNickname;
        this.ImgURL = imgURL;
        this.createDate = createDate;
        this.modifiedDate = modifiedDate;
    }
    public static ReviewProductJoinResponseDTO toReviewProductJoinResponseDTO(Review review){
        return ReviewProductJoinResponseDTO.builder()
                .reviewId(review.getId())
                .userNickname(review.getUserId().getNickname())
                .imgURL(review.getImageUrl())
                .title(review.getTitle())
                .content(review.getContent())
                .createDate(review.getCreateDate())
                .modifiedDate(review.getModifiedDate())
                .build();
    }



}
