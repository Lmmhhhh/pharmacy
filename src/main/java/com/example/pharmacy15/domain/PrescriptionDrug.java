package com.example.pharmacy15.domain;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PrescriptionDrug {
    private int id;
    private int prescriptionId;
    private int drugId;
    private String dosage;
    private String medicGuide;
}

