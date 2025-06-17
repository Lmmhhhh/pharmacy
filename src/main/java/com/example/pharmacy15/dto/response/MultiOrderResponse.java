package com.example.pharmacy15.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MultiOrderResponse {
    private int orderCount;
    private List<OrderResponse> orders;
    private String message;
    private String memberType;
}
