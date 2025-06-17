package com.example.pharmacy15.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;


// 일반약 주문
@Getter @Setter
public class OtcOrderRequest {

    private String phoneLast4;       // 비회원일 경우 null 가능
    private String name;             // 비회원일 경우 null 가능
    private LocalDate saleDate;

    private List<DrugItem> items;    // 주문한 약품 목록

    @Getter @Setter
    public static class DrugItem {
        private String drugName;
        private Integer quantity;
    }
}
