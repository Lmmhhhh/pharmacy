package com.example.pharmacy15.repository.iface;

public interface PrescriptionRepository {
    int save(int patientId, String doctor, String hospital, String issuedDate);
}