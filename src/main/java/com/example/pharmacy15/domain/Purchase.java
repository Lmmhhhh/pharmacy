package com.example.pharmacy15.domain;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Purchase {
    private int purchaseId;
    private int drugId;
    private int quantity;
    private int remainingQuantity;
    private BigDecimal unitPrice;
    private LocalDate purchaseDate;
    private LocalDate expirationDate;
    private String supplier;
}

