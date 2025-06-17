package com.example.pharmacy15.domain;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Patient {
    private int patientId;
    private String patientName;
    private LocalDate birthDate;
    private String gender;
    private String phone;
    private String address;
    private String allergy;
}
