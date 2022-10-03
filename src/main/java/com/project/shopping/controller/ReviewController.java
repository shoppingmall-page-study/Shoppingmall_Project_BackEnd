package com.project.shopping.controller;


import com.project.shopping.Error.CustomExcpetion;
import com.project.shopping.Error.ErrorCode;
import com.project.shopping.auth.PrincipalDetails;
import com.project.shopping.dto.ReviewDTO;
import com.project.shopping.dto.requestDTO.RevieRequestDTO.ReviewCreateRequestDTO;
import com.project.shopping.dto.requestDTO.RevieRequestDTO.ReviewUpdateRequestDTO;
import com.project.shopping.dto.responseDTO.ReviewResponseDTO.*;
import com.project.shopping.model.Product;
import com.project.shopping.model.Review;
import com.project.shopping.model.User;
import com.project.shopping.service.ProductService;
import com.project.shopping.service.ReviewService;
import com.project.shopping.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@RestController
public class ReviewController {
    @Autowired
    private ProductService productService;
    @Autowired
    private UserService userService;

    @Autowired
    private ReviewService reviewService;

    private String ActiveStatus= "active";

    // 리뷰 생성
    @PostMapping("/api/review/create/{id}")
    public ResponseEntity<?> createReview(Authentication authentication, @PathVariable(value = "id") int ProductId, @RequestBody ReviewCreateRequestDTO reviewCreateRequestDTO){
        if(authentication == null){
            throw  new CustomExcpetion("허용되지 않은 접근입니다." , ErrorCode.UnauthorizedException);
        }
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        String email = principalDetails.getUser().getEmail();

        // 로그인 정보로 user 찾기
        User user = userService.findEmailByUser(email);

        // 파라미터로 받은 값으로 상품 찾기
        Product product = productService.findproductid(ProductId);


        // 리뷰 생성
        Review review = Review.builder()
                .userId(user)
                .productId(product)
                .title(reviewCreateRequestDTO.getTitle())
                .content(reviewCreateRequestDTO.getContent())
                .imageUrl(reviewCreateRequestDTO.getImgUrl())
                .reviewcreateTime(LocalDate.now())
                .modifieddate(LocalDate.now())
                .status("active")
                .build();


        Review registeredReview = reviewService.create(review);


        // Review Dto 로 응답 보내기

        ReviewCreateResponseDTO reviewCreateResponseDTO = ReviewCreateResponseDTO.builder()
                .reviewId(registeredReview.getId())
                .title(registeredReview.getTitle())
                .content(registeredReview.getContent())
                .build();

        Map<String , Object> result = new HashMap<>();
        result.put("msg","리뷰 등록에 성공했습니다.");
        result.put("data", reviewCreateResponseDTO);

        return ResponseEntity.ok().body(result);
    }

    // 리뷰 삭제
    @DeleteMapping ("/api/review/delete/{id}")
    public ResponseEntity<?> reviewdelete(Authentication authentication, @PathVariable(value = "id") int ReviewId){ // reviewid
        if(authentication == null){
            throw  new CustomExcpetion("허용되지 않은 접근입니다." , ErrorCode.UnauthorizedException);
        }
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        String email = principalDetails.getUser().getEmail();
        User user = userService.findEmailByUser(email);
        if(!reviewService.existReviewUserAndId(user,ReviewId)){
            throw new CustomExcpetion("해당 리뷰가 존재하지 않습니다", ErrorCode.NotFoundReviewException);
        }
        Review findreview = reviewService.findReviewUserAndId(user, ReviewId);
        findreview.setStatus("Disabled");
        reviewService.update(findreview);

        ReviewDeleteResponseDTO reviewDeleteResponseDTO = ReviewDeleteResponseDTO.builder()
                .reviewId(findreview.getId())
                .content(findreview.getContent())
                .title(findreview.getTitle())
                .build();

        Map<String , Object> result = new HashMap<>();
        result.put("msg","리뷰 삭제에 성공했습니다.");
        result.put("data", reviewDeleteResponseDTO);
        return  ResponseEntity.ok().body(result);
    }

    // 내가 등록한 리뷰 조회
    @GetMapping("/api/review/user")
    public ResponseEntity<?> findUserReviewlist(Authentication authentication){
        if(authentication == null){
            throw  new CustomExcpetion("허용되지 않은 접근입니다." , ErrorCode.UnauthorizedException);
        }
        // 현재 로그인한 유저 찾기
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        String email = principalDetails.getUser().getEmail();
        User user = userService.findEmailByUser(email);

        // 현재 나의 리뷰 목록 조회
        List<Review> userReviewlist = reviewService.getEqUserAndActive(user,ActiveStatus);

        //dto 만들기
        List<ReviewUserJoinResponseDTO> userReviewListDto = new ArrayList<>();
        for (Review review : userReviewlist){
            ReviewUserJoinResponseDTO reviewUserJoinResponseDTO = ReviewUserJoinResponseDTO.builder()
                    .reviewId(review.getId())
                    .content(review.getContent())
                    .title(review.getTitle())
                    .createTime(review.getCreateTime())
                    .modifiedTime(review.getModifieddate())
                    .build();
            userReviewListDto.add(reviewUserJoinResponseDTO);
        }

        Map<String , Object> result = new HashMap<>();
        result.put("msg","리뷰 조회에 성공했습니다.");
        result.put("data",userReviewListDto);
        return ResponseEntity.ok().body(result);

    }

    // 상품에 달린 리뷰 조회
    @GetMapping("/api/review/{id}")
    public ResponseEntity<?> findProductReviewlist(@PathVariable(value = "id") int ProductId){
        // 상품 찾기
        Product product = productService.findproductid(ProductId);

        // 상품에 등록된 리뷰 찾기
        List<Review> ProductReviewList = reviewService.getEqProductAndActive(product, ActiveStatus);
        List<ReviewProductJoinResponseDTO> ProductReviewDto = new ArrayList<>();

        for(Review review : ProductReviewList){
            ReviewProductJoinResponseDTO reviewProductJoinResponseDTO = ReviewProductJoinResponseDTO.builder()
                    .reviewId(review.getId())
                    .userNicename(review.getUserId().getNickname())
                    .imgURL(review.getImageUrl())
                    .title(review.getTitle())
                    .content(review.getContent())
                    .createTime(review.getCreateTime())
                    .modifiedTime(review.getModifieddate())
                    .build();
            ProductReviewDto.add(reviewProductJoinResponseDTO);
        }
        Map<String, Object> result = new HashMap<>();
        result.put("msg","리뷰 조회에 성공했습니다.");
        result.put("data",ProductReviewDto);
        return ResponseEntity.ok().body(result);

    }

    @PutMapping("/api/review/update/{id}")
    public ResponseEntity<?> productUpdate(Authentication authentication, @PathVariable(value = "id") int reviewId, @RequestBody ReviewUpdateRequestDTO reviewUpdateRequestDTO){
        if(authentication == null){
            throw  new CustomExcpetion("허용되지 않은 접근입니다." , ErrorCode.UnauthorizedException);
        }
        PrincipalDetails principalDetails =  (PrincipalDetails) authentication.getPrincipal();
        String email = principalDetails.getUser().getEmail();
        User user = userService.findEmailByUser(email); // 유저 찾기

        Review findReview = reviewService.findReviewUserAndId(user,reviewId); // 유저와 해당 리뷰 아이디로 리뷰 찾기
        if(reviewUpdateRequestDTO.getContent() != ""){
            findReview.setContent(reviewUpdateRequestDTO.getContent());
        }
        if(reviewUpdateRequestDTO.getTitle() != ""){
            findReview.setTitle(reviewUpdateRequestDTO.getTitle());
        }
        if(reviewUpdateRequestDTO.getImgUrl() != ""){
            findReview.setImageUrl(reviewUpdateRequestDTO.getImgUrl());
        }

        findReview.builder().modifieddate(LocalDate.now());


        Review review = reviewService.update(findReview);

        ReviewUpdateResponseDTO reviewUpdateResponseDTO = ReviewUpdateResponseDTO.builder()
                .reviewId(review.getId())
                .title(review.getTitle())
                .content(review.getContent())
                .imgUrl(review.getImageUrl())
                .build();

        Map<String, Object> result = new HashMap<>();
        result.put("msg","리뷰수정에 성공했습니다.");
        result.put("data", reviewUpdateResponseDTO);

        return  ResponseEntity.ok().body(result);

    }





}
