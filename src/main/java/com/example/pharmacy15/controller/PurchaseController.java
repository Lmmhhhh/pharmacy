package com.example.pharmacy15.controller;

import com.example.pharmacy15.dto.request.PurchaseRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/purchases")
public class PurchaseController {

    private final JdbcTemplate jdbc;

    @PostMapping
    public ResponseEntity<Map<String, Object>> register(@RequestBody PurchaseRequest request) {
        // 약 이름으로 drug_id 조회
        String findIdSql = "SELECT drug_id FROM drug WHERE name = ?";
        Integer drugId = jdbc.queryForObject(findIdSql, Integer.class, request.getDrugName());

        // INSERT 실행
        String sql = """
            INSERT INTO purchase (drug_id, purchase_date, expiration_date, quantity, remaining_quantity, unit_price, supplier)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;
        jdbc.update(sql,
                drugId,
                request.getPurchaseDate(),
                request.getExpirationDate(),
                request.getQuantity(),
                request.getQuantity(), // 처음엔 입고 수량 = 남은 수량
                request.getUnitPrice(),
                request.getSupplier());

        // 마지막 등록된 입고 정보 반환
        String lastSql = "SELECT * FROM purchase ORDER BY purchase_id DESC LIMIT 1";
        Map<String, Object> latest = jdbc.queryForMap(lastSql);

        return ResponseEntity.ok(latest);
    }
}