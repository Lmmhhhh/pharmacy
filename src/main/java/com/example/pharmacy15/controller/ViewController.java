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

    @GetMapping("/drug-stock")
    public ResponseEntity<List<Map<String, Object>>> drugStock() {
        return ResponseEntity.ok(jdbc.queryForList("SELECT * FROM drug_stock_view"));
    }

    @GetMapping("/expiring")
    public ResponseEntity<List<Map<String, Object>>> expiring() {
        return ResponseEntity.ok(jdbc.queryForList("SELECT * FROM view_expiring_list"));
    }

    @GetMapping("/low_stock")
    public ResponseEntity<List<Map<String, Object>>> salesSummary() {
        return ResponseEntity.ok(jdbc.queryForList("SELECT * FROM view_low_stock"));
    }

    @GetMapping("/patient-history/{id}")
    public ResponseEntity<List<Map<String, Object>>> patientHistory(@PathVariable int id) {
        String sql = "SELECT * FROM view_patient_history WHERE patient_id = ?";
        return ResponseEntity.ok(jdbc.queryForList(sql, id));
    }
}