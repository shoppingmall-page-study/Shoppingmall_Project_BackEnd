package com.project.shopping.dto.requestDTO.RevieRequestDTO;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
public class ReviewCreateRequestDTO {
    //review
    private  String imgUrl;
    private  String title;
    private  String content;

    @Builder

    public ReviewCreateRequestDTO(String imgUrl, String title, String content) {
        this.imgUrl = imgUrl;
        this.title = title;
        this.content = content;
    }
}
