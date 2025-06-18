package com.example.pharmacy15.repository.iface;

public interface PrescriptionDrugRepository {
    void save(int prescriptionId, int drugId, String dosage, String medicGuide);
}
