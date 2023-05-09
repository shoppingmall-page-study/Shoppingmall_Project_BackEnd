package com.project.shopping.dto.requestDTO.PaymentRequestDTO;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import javax.validation.constraints.NotBlank;

@RequiredArgsConstructor
@Getter
public class PaymentRequestDTO {

    @NotBlank
    private String impUid;

    @NotBlank
    private String orderId;

}
