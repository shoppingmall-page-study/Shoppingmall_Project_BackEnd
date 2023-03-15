package com.project.shopping.service;

import com.project.shopping.Error.CustomException;
import com.project.shopping.Error.ErrorCode;
import com.project.shopping.dto.requestDTO.RevieRequestDTO.ReviewCreateRequestDTO;
import com.project.shopping.dto.requestDTO.RevieRequestDTO.ReviewUpdateRequestDTO;
import com.project.shopping.dto.responseDTO.ReviewResponseDTO.*;
import com.project.shopping.model.Product;
import com.project.shopping.model.Review;
import com.project.shopping.model.User;
import com.project.shopping.repository.ProductRepository;
import com.project.shopping.repository.ReviewRepository;
import com.project.shopping.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {
    // 삭제 , 내 리뷰목록 조회 , 생성 , 상품 리뷰목록  조회


    private final  ReviewRepository reviewrepository;
    private  final UserRepository userRepository;
    private  final ProductRepository productRepository;

    // 생성
    public ReviewCreateResponseDTO create(User user, int ProductId, ReviewCreateRequestDTO reviewCreateRequestDTO){

        String email = user.getEmail();

        // 로그인 정보로 user 찾기
        userRepository.findByEmail(email)
                .orElseThrow(()-> new CustomException(ErrorCode.NotFoundUserException));

        // 파라미터로 받은 값으로 상품 찾기
        Product product = productRepository.findById(ProductId)
                .orElseThrow(()-> new CustomException(ErrorCode.NotFoundProductException));


        // 리뷰 생성
        Review review = Review.builder()
                .userId(user)
                .productId(product)
                .title(reviewCreateRequestDTO.getTitle())
                .content(reviewCreateRequestDTO.getContent())
                .imageUrl(reviewCreateRequestDTO.getImgUrl())
                .status("active")
                .build();

        reviewrepository.save(review);
        ReviewCreateResponseDTO reviewCreateResponseDTO = ReviewCreateResponseDTO.builder()
                .reviewId(review.getId())
                .title(review.getTitle())
                .content(review.getContent())
                .build();


        return reviewCreateResponseDTO;
    }


    public ReviewUpdateResponseDTO update(User user, int reviewId, ReviewUpdateRequestDTO reviewUpdateRequestDTO){

        userRepository.findByEmail(user.getEmail())
                .orElseThrow(()-> new CustomException(ErrorCode.NotFoundUserException));// 유저 찾기

        Review findReview = reviewrepository.findByUserIdAndId(user,reviewId)
                .orElseThrow(()-> new CustomException(ErrorCode.NotFoundReviewException));// 유저와 해당 리뷰 아이디로 리뷰 찾기

        findReview.setContent(reviewUpdateRequestDTO.getContent());
        findReview.setTitle(reviewUpdateRequestDTO.getTitle());
        findReview.setImageUrl(reviewUpdateRequestDTO.getImgUrl());

        // 수정하기
        if(findReview.getProductId().getStatus().equals("Disabled")){
            throw new CustomException(ErrorCode.NotFoundProductException);
        }

        reviewrepository.save(findReview);
        ReviewUpdateResponseDTO reviewUpdateResponseDTO = ReviewUpdateResponseDTO.builder()
                .reviewId(findReview.getId())
                .title(findReview.getTitle())
                .content(findReview.getContent())
                .imgUrl(findReview.getImageUrl())
                .build();


        return reviewUpdateResponseDTO;}



    // 삭제
    public ReviewDeleteResponseDTO deleteReview(User user,int ReviewId){

        userRepository.findByEmail(user.getEmail())
                .orElseThrow(()-> new CustomException(ErrorCode.NotFoundUserException));

        Review findreview = reviewrepository.findByUserIdAndId(user, ReviewId)
                .orElseThrow(()-> new CustomException(ErrorCode.NotFoundReviewException));
        findreview.setStatus("Disabled");

        reviewrepository.save(findreview);


        ReviewDeleteResponseDTO reviewDeleteResponseDTO = ReviewDeleteResponseDTO.builder()
                .reviewId(findreview.getId())
                .content(findreview.getContent())
                .title(findreview.getTitle())
                .build();

        return reviewDeleteResponseDTO;
    }



    public List<ReviewUserJoinResponseDTO>  getEqUserAndActive(User user, String status){

        userRepository.findByEmail(user.getEmail())
                .orElseThrow(()-> new CustomException(ErrorCode.NotFoundUserException));
        List<Review> userReviewlist = reviewrepository.getEqUserAndActive(user, status);
        //dto 만들기
        List<ReviewUserJoinResponseDTO> userReviewListDto = new ArrayList<>();
        for (Review review : userReviewlist){
            ReviewUserJoinResponseDTO reviewUserJoinResponseDTO = ReviewUserJoinResponseDTO.builder()
                    .reviewId(review.getId())
                    .content(review.getContent())
                    .title(review.getTitle())
                    .createDate(review.getCreateDate())
                    .modifiedDate(review.getModifiedDate())
                    .imgUrl(review.getImageUrl())
                    .build();
            userReviewListDto.add(reviewUserJoinResponseDTO);
        }
        return userReviewListDto;
    }

    public List<ReviewProductJoinResponseDTO> getEqProductAndActive(int ProductId, String status){
        Product product = productRepository.findById(ProductId)
                .orElseThrow(()-> new CustomException(ErrorCode.NotFoundProductException));
        List<Review> ProductReviewList = reviewrepository.getEqProductAndActive(product,status);

        List<ReviewProductJoinResponseDTO> ProductReviewDto = new ArrayList<>();

        for(Review review : ProductReviewList){
            ReviewProductJoinResponseDTO reviewProductJoinResponseDTO = ReviewProductJoinResponseDTO.builder()
                    .reviewId(review.getId())
                    .userNickname(review.getUserId().getNickname())
                    .imgURL(review.getImageUrl())
                    .title(review.getTitle())
                    .content(review.getContent())
                    .createDate(review.getCreateDate())
                    .modifiedDate(review.getModifiedDate())
                    .build();
            ProductReviewDto.add(reviewProductJoinResponseDTO);
        }

        return  ProductReviewDto;
    }
}
