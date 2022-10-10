package com.project.shopping.dto.requestDTO.OrderRequestDTO;

import com.project.shopping.model.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@NoArgsConstructor
@Getter
public class OrderRequestDTO {
    private ArrayList<Integer> productsId;
    private ArrayList<Integer> productsNumber;


    public OrderRequestDTO(ArrayList<Integer> productsId, ArrayList<Integer> productsNumber) {
        this.productsId = productsId;
        this.productsNumber = productsNumber;
    }
}



