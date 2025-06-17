package com.example.pharmacy15.repository;

public interface PrescriptionRepository {
    int save(int patientId, String doctor, String hospital, String issuedDate);
}