package com.example.pharmacy15.domain;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Drug {
    private int drugId;
    private String name;
    private String ingredient;
    private String company;
    private String prescribeType;
    private String efficacy;
    private String dosage;
    private String caution;
    private String storing;
    private int totalSales;
}
