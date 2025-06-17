package com.example.pharmacy15.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

//일반약 주문 응답

@Getter @Setter
@AllArgsConstructor
public class OrderResponse {
    private Integer orderId;
    private Integer totalPrice;
}
