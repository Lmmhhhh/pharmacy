package com.example.pharmacy15.repository;

public interface PrescriptionDrugRepository {
    void save(int prescriptionId, int drugId, String dosage, String medicGuide);
}
