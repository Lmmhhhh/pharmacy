package com.example.pharmacy15.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
//일반약 주문 응답

@Getter @Setter
@AllArgsConstructor
public class OrderResponse {
    private Integer orderId;
    private Integer totalPrice;
    private LocalDate saleDate;
}
