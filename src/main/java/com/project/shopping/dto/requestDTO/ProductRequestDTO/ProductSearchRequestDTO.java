package com.project.shopping.dto.requestDTO.ProductRequestDTO;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ProductSearchRequestDTO {
    private  String keyword;

    @Builder

    public ProductSearchRequestDTO(String keyword) {
        this.keyword = keyword;
    }
}
