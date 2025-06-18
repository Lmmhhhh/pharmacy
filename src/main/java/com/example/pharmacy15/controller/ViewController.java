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
    public ResponseEntity<?> drugStock() {
        try {
            List<Map<String, Object>> result = jdbc.queryForList("SELECT * FROM drug_stock_view");
            if (result.isEmpty()) {
                return ResponseEntity.status(404).body(Map.of(
                        "status", 404,
                        "message", "재고 정보를 찾을 수 없습니다."
                ));
            }
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    // 유통기한 임박 약품 리스트
    @GetMapping("/expiring")
    public ResponseEntity<?> expiring() {
        try {
            List<Map<String, Object>> result = jdbc.queryForList("SELECT * FROM view_expiring_list");
            if (result.isEmpty()) {
                return ResponseEntity.status(404).body(Map.of(
                        "status", 404,
                        "message", "유통기한 임박 약품이 없습니다."
                ));
            }
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    // 재고 부족 약품 리스트
    @GetMapping("/low_stock")
    public ResponseEntity<?> salesSummary() {
        try {
            List<Map<String, Object>> result = jdbc.queryForList("SELECT * FROM view_low_stock");
            if (result.isEmpty()) {
                return ResponseEntity.status(404).body(Map.of(
                        "status", 404,
                        "message", "재고 부족 약품이 없습니다."
                ));
            }
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    // 회원 복약 히스토리
    @GetMapping("/patient-history")
    public ResponseEntity<?> patientHistoryByNameAndPhone(
            @RequestParam String name,
            @RequestParam String phoneLast4) {

        String sql = """
            SELECT *
            FROM view_patient_history
            WHERE patient_name = ?
              AND RIGHT(phone, 4) = ?
        """;

        try {
            List<Map<String, Object>> result = jdbc.queryForList(sql, name, phoneLast4);
            if (result.isEmpty()) {
                return ResponseEntity.status(404).body(Map.of(
                        "status", 404,
                        "message", "회원 정보가 존재하지 않거나 복약 이력이 없습니다."
                ));
            }
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    // 키워드로 약품 조회
    @GetMapping("/search")
    public ResponseEntity<?> searchDrug(@RequestParam String keyword) {
        String sql = "SELECT * FROM drug WHERE name LIKE ? OR company LIKE ? OR efficacy LIKE ?";
        Object[] params = {"%" + keyword + "%", "%" + keyword + "%", "%" + keyword + "%"};

        try {
            List<Map<String, Object>> result = jdbc.queryForList(sql, params);
            if (result.isEmpty()) {
                return ResponseEntity.status(404).body(Map.of(
                        "status", 404,
                        "message", "검색 결과가 없습니다. 다른 키워드를 입력해 주세요."
                ));
            }
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}