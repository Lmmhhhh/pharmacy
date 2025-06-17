package com.example.pharmacy15.dto.request;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter @Setter
public class PurchaseRequest {
    private int drugId;
    private int quantity;
    private int unitPrice;
    private LocalDate purchaseDate;
    private LocalDate expirationDate;
    private String supplier;
}
