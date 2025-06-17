package com.example.pharmacy15.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class PrescriptionOrderRequest {

    // 환자 정보
    private String patientName;
    private LocalDate birthDate;
    private String gender;
    private String phone;
    private String address;
    private String allergy;

    // 처방전 정보
    private String doctor;
    private String hospital;
    private LocalDate issuedDate;

    // 주문 정보
    private LocalDate saleDate;

    private List<PrescriptionDrugItem> items;

    @Getter
    @Setter
    public static class PrescriptionDrugItem {
        private String drugName;
        private Integer quantity;
        private String dosage;
        private String medicGuide;
    }
}
