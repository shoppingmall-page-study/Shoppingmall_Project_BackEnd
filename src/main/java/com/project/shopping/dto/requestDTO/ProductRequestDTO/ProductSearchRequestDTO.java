package com.project.shopping.dto.requestDTO.ProductRequestDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@Getter
public class ProductSearchRequestDTO {

    @NotBlank
    private  String keyword;

}
