package com.project.shopping.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ProductRequestDTO {
    private String title;
    private  String content;
    private  String name;
    private  long price;
    private  int total;

    @Builder
    public ProductRequestDTO(String title, String content, String name, long price, int total) {
        this.title = title;
        this.content = content;
        this.name = name;
        this.price = price;
        this.total = total;
    }
}
