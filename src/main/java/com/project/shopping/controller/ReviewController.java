package com.project.shopping.controller;


import com.project.shopping.Error.CustomException;
import com.project.shopping.Error.ErrorCode;
import com.project.shopping.auth.PrincipalDetails;
import com.project.shopping.dto.requestDTO.RevieRequestDTO.ReviewCreateRequestDTO;
import com.project.shopping.dto.requestDTO.RevieRequestDTO.ReviewUpdateRequestDTO;
import com.project.shopping.dto.responseDTO.ReviewResponseDTO.*;
import com.project.shopping.model.User;
import com.project.shopping.service.ProductService;
import com.project.shopping.service.ReviewService;
import com.project.shopping.service.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequiredArgsConstructor
public class ReviewController {


    private final ReviewService reviewService;

    private String ActiveStatus= "active";

    // 리뷰 생성
    @PostMapping("/api/review/create/{id}")
    public ResponseEntity<?> createReview(Authentication authentication, @PathVariable(value = "id") int ProductId, @RequestBody @Valid ReviewCreateRequestDTO reviewCreateRequestDTO){

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        User user = principalDetails.getUser();

        ReviewCreateResponseDTO reviewCreateResponseDTO = reviewService.create(user, ProductId, reviewCreateRequestDTO);

        // Review Dto 로 응답 보내기

        Map<String , Object> result = new HashMap<>();
        result.put("msg","리뷰 등록에 성공했습니다.");
        result.put("data", reviewCreateResponseDTO);

        return ResponseEntity.ok().body(result);
    }

    // 리뷰 삭제
    @DeleteMapping ("/api/review/delete/{id}")
    public ResponseEntity<?> reviewDelete(Authentication authentication, @PathVariable(value = "id") int ReviewId){ // reviewid

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        User user = principalDetails.getUser();

        ReviewDeleteResponseDTO reviewDeleteResponseDTO = reviewService.deleteReview(user, ReviewId);

        Map<String , Object> result = new HashMap<>();
        result.put("msg","리뷰 삭제에 성공했습니다.");
        result.put("data", reviewDeleteResponseDTO);
        return  ResponseEntity.ok().body(result);
    }

    // 내가 등록한 리뷰 조회
    @GetMapping("/api/review/user")
    public ResponseEntity<?> findUserReviewList(Authentication authentication){
        // 현재 로그인한 유저 찾기
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        User user = principalDetails.getUser();

        // 현재 나의 리뷰 목록 조회
        List<ReviewUserJoinResponseDTO>  userReviewlist = reviewService.getEqUserAndActive(user,ActiveStatus);

        Map<String , Object> result = new HashMap<>();
        result.put("msg","리뷰 조회에 성공했습니다.");
        result.put("data",userReviewlist);
        return ResponseEntity.ok().body(result);

    }

    // 상품에 달린 리뷰 조회
    @GetMapping("/api/review/{id}")
    public ResponseEntity<?> findProductReviewList(@PathVariable(value = "id") int ProductId){


        // 상품에 등록된 리뷰 찾기
        List<ReviewProductJoinResponseDTO> reviewProductJoinResponseDTOS = reviewService.getEqProductAndActive(ProductId, ActiveStatus);


        Map<String, Object> result = new HashMap<>();
        result.put("msg","리뷰 조회에 성공했습니다.");
        result.put("data",reviewProductJoinResponseDTOS);
        return ResponseEntity.ok().body(result);

    }

    @PutMapping("/api/review/update/{id}")
    public ResponseEntity<?> productReviewUpdate(Authentication authentication, @PathVariable(value = "id") int reviewId, @RequestBody @Valid ReviewUpdateRequestDTO reviewUpdateRequestDTO){

        PrincipalDetails principalDetails =  (PrincipalDetails) authentication.getPrincipal();
        User user = principalDetails.getUser();

        ReviewUpdateResponseDTO reviewUpdateResponseDTO = reviewService.update(user, reviewId, reviewUpdateRequestDTO);

        Map<String, Object> result = new HashMap<>();
        result.put("msg","리뷰수정에 성공했습니다.");
        result.put("data", reviewUpdateResponseDTO);

        return  ResponseEntity.ok().body(result);

    }

}
