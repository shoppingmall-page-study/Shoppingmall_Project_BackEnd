package com.project.shopping.dto.responseDTO.CartResponseDTO;


import com.project.shopping.model.Product;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ProductResponseDTO {
    private  int productId;
    private  String title;
    private  String name;
    private  String content;
    private String imgUrl;
    private  long price;

    @Builder
    private ProductResponseDTO(int productId, String title, String name, String content, String imgUrl, long price) {
        this.productId = productId;
        this.title = title;
        this.name = name;
        this.content = content;
        this.imgUrl = imgUrl;
        this.price = price;
    }

    public static ProductResponseDTO toProductResponseDTO(Product product){
        return ProductResponseDTO.builder()
                .productId(product.getId())
                .title(product.getTitle())
                .content(product.getContent())
                .name(product.getName())
                .imgUrl(product.getImgUrl())
                .price(product.getPrice())
                .build();
    }

}
