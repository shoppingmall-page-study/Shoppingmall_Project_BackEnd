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
    private  final ProductRepository productRepository;

    // 생성
    public ReviewCreateResponseDTO create(User user, int ProductId, ReviewCreateRequestDTO reviewCreateRequestDTO){


        // 파라미터로 받은 값으로 상품 찾기
        Product product = productRepository.findById(ProductId)
                .orElseThrow(()-> new CustomException(ErrorCode.NotFoundProductException));


        // 리뷰 생성 dto-> entity
        Review review = reviewCreateRequestDTO.toEntity(user, product,"active");
        reviewrepository.save(review);

        // entity -> dto
        return ReviewCreateResponseDTO.toReviewCreateResponseDTO(review);
    }


    public ReviewUpdateResponseDTO update(User user, int reviewId, ReviewUpdateRequestDTO reviewUpdateRequestDTO){


        Review review = reviewrepository.findByUserIdAndId(user,reviewId)
                .orElseThrow(()-> new CustomException(ErrorCode.NotFoundReviewException));// 유저와 해당 리뷰 아이디로 리뷰 찾기

        review.update(reviewUpdateRequestDTO.toEntity());

        // 수정하기
        if(review.getProductId().getStatus().equals("Disabled")){
            throw new CustomException(ErrorCode.NotFoundProductException);
        }
        reviewrepository.save(review);
        return ReviewUpdateResponseDTO.toReviewUpdateResponseDTO(review);
    }



    // 삭제
    public ReviewDeleteResponseDTO deleteReview(User user,int ReviewId){

        Review review = reviewrepository.findByUserIdAndId(user, ReviewId)
                .orElseThrow(()-> new CustomException(ErrorCode.NotFoundReviewException));
        review.delete();
        reviewrepository.save(review);

        return ReviewDeleteResponseDTO.toReviewDeleteResponseDTO(review);
    }


    public List<ReviewUserJoinResponseDTO>  getEqUserAndActive(User user, String status){


        List<Review> userReviewlist = reviewrepository.getEqUserAndActive(user, status);
        //dto 만들기
        List<ReviewUserJoinResponseDTO> reviewUserJoinResponseDTOS = new ArrayList<>();
        for (Review review : userReviewlist){
            reviewUserJoinResponseDTOS.add(ReviewUserJoinResponseDTO.toReviewUserJoinResponseDTO(review));
        }
        return reviewUserJoinResponseDTOS;
    }

    public List<ReviewProductJoinResponseDTO> getEqProductAndActive(int ProductId, String status){
        Product product = productRepository.findById(ProductId)
                .orElseThrow(()-> new CustomException(ErrorCode.NotFoundProductException));
        List<Review> ProductReviewList = reviewrepository.getEqProductAndActive(product,status);

        List<ReviewProductJoinResponseDTO> reviewProductJoinResponseDTOS = new ArrayList<>();

        for(Review review : ProductReviewList){
            reviewProductJoinResponseDTOS.add( ReviewProductJoinResponseDTO.toReviewProductJoinResponseDTO(review));
        }

        return  reviewProductJoinResponseDTOS;
    }
}
