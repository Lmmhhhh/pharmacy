package com.example.pharmacy15.domain;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Prescription {
    private int prescriptionId;
    private int patientId;
    private String doctor;
    private String hospital;
    private LocalDate issuedDate;
    private LocalDate validUntil;
    private String status;  // "조제 전", "조제 완료"
}