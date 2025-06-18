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
public class UpdateController {

    private final JdbcTemplate jdbc;

    @PutMapping("/patient/update")
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

    // 주문 삭제 (일반약만 가능)
    @DeleteMapping("/delete-od/{orderId}")
    public ResponseEntity<?> deleteOrder(@PathVariable int orderId) {
        try {
            // 0. 처방 연동 여부 확인
            String presSql = "SELECT prescription_id FROM orders WHERE order_id = ?";
            Integer presId = jdbc.queryForObject(presSql, Integer.class, orderId);
            if (presId != null) {
                return ResponseEntity.status(403).body(Map.of("message", "처방약 주문은 취소할 수 없습니다."));
            }

            // 1. 주문 존재 여부 확인
            String checkSql = "SELECT COUNT(*) FROM orders WHERE order_id = ?";
            Integer count = jdbc.queryForObject(checkSql, Integer.class, orderId);
            if (count == null || count == 0) {
                return ResponseEntity.status(404).body(Map.of("message", "해당 주문이 존재하지 않습니다."));
            }

            // 1.5 판매량 복원 (drug.total_sales 감소)
            String rollbackSales = """
                UPDATE drug d
                JOIN order_drug od ON d.drug_id = od.drug_id
                SET d.total_sales = d.total_sales - od.sale_quantity
                WHERE od.order_id = ?
            """;
            jdbc.update(rollbackSales, orderId);

            // 2. 재고 복원 (order_drug -> purchase)
            String restoreSql = """
                UPDATE purchase p
                JOIN order_drug od ON p.drug_id = od.drug_id
                SET p.remaining_quantity = p.remaining_quantity + od.sale_quantity
                WHERE od.order_id = ?
            """;
            jdbc.update(restoreSql, orderId);

            // 3. order_drug, orders 삭제
            jdbc.update("DELETE FROM order_drug WHERE order_id = ?", orderId);
            jdbc.update("DELETE FROM orders WHERE order_id = ?", orderId);

            return ResponseEntity.ok(Map.of("message", "주문이 취소되었습니다.", "order_id", orderId));

        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "삭제 중 오류 발생"));
        }
    }
}