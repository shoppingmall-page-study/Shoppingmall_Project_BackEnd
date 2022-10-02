package com.project.shopping.dto.requestDTO.RevieRequestDTO;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ReviewUpdateRequestDTO {
    private  String imgUrl;
    private  String title;
    private  String content;

    @Builder

    public ReviewUpdateRequestDTO(String imgUrl, String title, String content) {
        this.imgUrl = imgUrl;
        this.title = title;
        this.content = content;
    }
}
