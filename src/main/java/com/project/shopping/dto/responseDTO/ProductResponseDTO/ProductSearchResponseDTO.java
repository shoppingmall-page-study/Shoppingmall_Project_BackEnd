package com.project.shopping.dto.responseDTO.ProductResponseDTO;

import com.project.shopping.model.Product;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ProductSearchResponseDTO {
    private  int productId;
    private String title;
    private  String content;
    private  String name;
    private  long price;
    private  int total;
    private String imgUrl;
    private LocalDateTime createDate;
    private LocalDateTime modifiedDate;

    @Builder
    private ProductSearchResponseDTO(int productId, String title, String content, String name, long price, int total, String imgUrl, LocalDateTime createDate, LocalDateTime modifiedDate) {
        this.productId = productId;
        this.title = title;
        this.content = content;
        this.name = name;
        this.price = price;
        this.total = total;
        this.imgUrl = imgUrl;
        this.createDate = createDate;
        this.modifiedDate = modifiedDate;
    }


    public static  ProductSearchResponseDTO toProductSearchResponseDTO(Product product){
        return ProductSearchResponseDTO.builder()
                .productId(product.getId())
                .title(product.getTitle())
                .content(product.getContent())
                .name(product.getName())
                .price(product.getPrice())
                .total(product.getTotal())
                .imgUrl(product.getImgUrl())
                .createDate(product.getCreateDate())
                .modifiedDate(product.getModifiedDate())
                .build();
    }
}
