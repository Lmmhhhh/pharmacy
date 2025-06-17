package com.example.pharmacy15.domain;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private int orderId;
    private Integer patientId; // 비회원일 경우 null 허용
    private LocalDate saleDate;
    private int totalPrice;
}
