package com.example.pharmacy15.repository.implement;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import com.example.pharmacy15.repository.iface.PrescriptionDrugRepository;

@Repository
@RequiredArgsConstructor
public class PrescriptionDrugRepositoryImpl implements PrescriptionDrugRepository{

    private final JdbcTemplate jdbc;

    @Override
    public void save(int prescriptionId, int drugId, String dosage, String medicGuide) {
        String sql = "INSERT INTO prescription_drug (prescription_id, drug_id, dosage, medic_guide) VALUES (?, ?, ?, ?)";
        jdbc.update(sql, prescriptionId, drugId, dosage, medicGuide);
    }
}
