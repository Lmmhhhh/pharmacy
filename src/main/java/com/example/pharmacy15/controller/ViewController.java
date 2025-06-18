package com.example.pharmacy15.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/view")
public class ViewController {

    private final JdbcTemplate jdbc;

    // 약품별 재고 조회
    @GetMapping("/drug-stock")
    public ResponseEntity<List<Map<String, Object>>> drugStock() {
        return ResponseEntity.ok(jdbc.queryForList("SELECT * FROM drug_stock_view"));
    }

    // 유통기한 임박 약품 리스트
    @GetMapping("/expiring")
    public ResponseEntity<List<Map<String, Object>>> expiring() {
        return ResponseEntity.ok(jdbc.queryForList("SELECT * FROM view_expiring_list"));
    }

    // 재고 부족 약품 리스트
    @GetMapping("/low_stock")
    public ResponseEntity<List<Map<String, Object>>> salesSummary() {
        return ResponseEntity.ok(jdbc.queryForList("SELECT * FROM view_low_stock"));
    }

    // 회원 복약 히스토리
    @GetMapping("/patient-history/{id}")
    public ResponseEntity<List<Map<String, Object>>> patientHistory(@PathVariable int id) {
        String sql = "SELECT * FROM view_patient_history WHERE patient_id = ?";
        return ResponseEntity.ok(jdbc.queryForList(sql, id));
    }

    // 키워드로 약품 조회
    @GetMapping("/search")
    public ResponseEntity<List<Map<String, Object>>> searchDrug(@RequestParam String keyword) {
        String sql = "SELECT * FROM drug WHERE name LIKE ? OR company LIKE ? OR efficacy LIKE ?";
        Object[] params = {"%" + keyword + "%", "%" + keyword + "%", "%" + keyword + "%"};

        return ResponseEntity.ok(jdbc.queryForList(sql, params));
    }
}