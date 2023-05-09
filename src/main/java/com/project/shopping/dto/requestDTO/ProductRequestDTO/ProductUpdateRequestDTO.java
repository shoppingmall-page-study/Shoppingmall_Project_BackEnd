package com.project.shopping.dto.requestDTO.ProductRequestDTO;


import com.project.shopping.model.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
public class ProductUpdateRequestDTO {
    @NotBlank
    private String title;
    @NotBlank
    private  String content;
    @NotBlank
    private  String name;
    @NotNull
    private  long price;

    @NotNull
    private  int total;
    @NotBlank
    private String imgUrl;

    public Product toEntity(){
        return Product.builder()
                .title(this.title)
                .content(this.content)
                .name(this.name)
                .price(this.price)
                .total(this.total)
                .imgUrl(this.imgUrl)
                .build();
    }

}
