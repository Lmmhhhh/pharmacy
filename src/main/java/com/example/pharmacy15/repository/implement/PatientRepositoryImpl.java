package com.example.pharmacy15.repository.implement;

import com.example.pharmacy15.domain.Patient;
import com.example.pharmacy15.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PatientRepositoryImpl implements PatientRepository {

    private final JdbcTemplate jdbc;

    private final RowMapper<Patient> patientRowMapper = (rs, rowNum) -> {
        Patient p = new Patient();
        p.setPatientId(rs.getInt("patient_id"));
        p.setPatientName(rs.getString("patient_name"));
        p.setBirthDate(rs.getDate("birth_date").toLocalDate());
        p.setGender(rs.getString("gender"));
        p.setPhone(rs.getString("phone"));
        p.setAddress(rs.getString("address"));
        p.setAllergy(rs.getString("allergy"));
        return p;
    };

    @Override
    public Optional<Patient> findByNameAndPhoneLast4(String name, String phoneLast4) {
        String sql = "SELECT * FROM patient WHERE patient_name = ? AND RIGHT(phone, 4) = ?";
        return jdbc.query(sql, patientRowMapper, name, phoneLast4)
                .stream().findFirst();
    }
}
