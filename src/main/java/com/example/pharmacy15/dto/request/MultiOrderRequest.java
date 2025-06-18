package com.example.pharmacy15.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class MultiOrderRequest {
    private boolean isPrescription;
    private String patientName;
    private LocalDate birthDate;
    private String gender;
    private String phone;
    private String address;
    private String allergy;
    private String doctor;
    private String hospital;
    private LocalDate issuedDate;
    private LocalDate saleDate;
    private List<DrugItem> items;

    @Getter
    @Setter
    public static class DrugItem {
        private String drugName;        // 사용자 입력 기준
        private Integer quantity;
        private String dosage;          // 처방약일 경우
        private String medicGuide;      // 처방약일 경우
    }
}



