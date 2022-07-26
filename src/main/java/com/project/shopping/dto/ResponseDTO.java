package com.project.shopping.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDTO<T> {
    private String error;
    private List<T> data;
}
