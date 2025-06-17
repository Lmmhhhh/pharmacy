package com.example.pharmacy15.domain;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDrug {
    private int id;
    private int orderId;
    private int drugId;
    private int saleQuantity;
    private int salePrice;
}
