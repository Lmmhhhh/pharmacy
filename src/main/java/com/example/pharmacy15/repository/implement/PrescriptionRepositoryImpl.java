package com.example.pharmacy15.repository.implement;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import com.example.pharmacy15.repository.PrescriptionRepository;

import java.sql.PreparedStatement;
import java.sql.Statement;

@Repository
@RequiredArgsConstructor
public class PrescriptionRepositoryImpl implements PrescriptionRepository {

    private final JdbcTemplate jdbc;

    @Override
    public int save(int patientId, String doctor, String hospital, String issuedDate) {
        String sql = "INSERT INTO prescription (patient_id, doctor, hospital, issued_date, status) VALUES (?, ?, ?, ?, '조제 대기')";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbc.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, patientId);
            ps.setString(2, doctor);
            ps.setString(3, hospital);
            ps.setString(4, issuedDate);
            return ps;
        }, keyHolder);

        return keyHolder.getKey().intValue(); // 생성된 prescription_id 반환
    }
}

