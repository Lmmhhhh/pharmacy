package com.example.pharmacy15.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/patient")
public class UpdatePatient {

    private final JdbcTemplate jdbc;

    @PutMapping("/update")
    public ResponseEntity<Map<String, Object>> updatePatient(
            @RequestParam String name,
            @RequestParam String phoneLast4,
            @RequestBody Map<String, Object> body) {

        // 환자 ID 조회
        Integer patientId = null;
        try {
            String findSql = "SELECT patient_id FROM patient WHERE patient_name = ? AND RIGHT(phone, 4) = ?";
            patientId = jdbc.queryForObject(findSql, Integer.class, name, phoneLast4);
        } catch (Exception e) {
            return ResponseEntity.status(404).body(Map.of("message", "해당 환자를 찾을 수 없습니다."));
        }

        // 업데이트할 필드 수집
        String sql = "UPDATE patient SET ";
        List<Object> params = new ArrayList<>();
        List<String> updates = new ArrayList<>();

        if (body.containsKey("address") && body.get("address") != null) {
            updates.add("address = ?");
            params.add(body.get("address"));
        }
        if (body.containsKey("allergy") && body.get("allergy") != null) {
            updates.add("allergy = ?");
            params.add(body.get("allergy"));
        }
        if (body.containsKey("phone") && body.get("phone") != null) {
            updates.add("phone = ?");
            params.add(body.get("phone"));
        }

        if (updates.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "변경할 내용이 없습니다."));
        }

        sql += String.join(", ", updates) + " WHERE patient_id = ?";
        params.add(patientId);

        int updated = jdbc.update(sql, params.toArray());

        String resultSql = "SELECT patient_id, patient_name, phone, address, allergy FROM patient WHERE patient_id = ?";
        Map<String, Object> updatedPatient = jdbc.queryForMap(resultSql, patientId);

        return ResponseEntity.ok(updatedPatient);
    }
}