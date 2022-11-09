package com.project.shopping.dto.requestDTO.PaymentRequestDTO;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class PaymentRequestDTO {
    private String impUid;
    private String orderId;

    @Builder
    public PaymentRequestDTO(String impUid, String orderId) {
        this.impUid = impUid;
        this.orderId = orderId;
    }
}
