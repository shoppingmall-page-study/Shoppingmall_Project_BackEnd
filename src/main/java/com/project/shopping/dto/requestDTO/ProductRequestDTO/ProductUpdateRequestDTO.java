package com.project.shopping.dto.requestDTO.ProductRequestDTO;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ProductUpdateRequestDTO {
    private String title;
    private  String content;
    private  String name;
    private  long price;
    private  int total;
    private String imgUrl;

    @Builder

    public ProductUpdateRequestDTO(String title, String content, String name, long price, int total, String imgUrl) {
        this.title = title;
        this.content = content;
        this.name = name;
        this.price = price;
        this.total = total;
        this.imgUrl = imgUrl;
    }
}
