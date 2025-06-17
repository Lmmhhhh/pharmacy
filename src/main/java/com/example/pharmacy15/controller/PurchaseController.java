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
        String sql = "INSERT INTO purchase (drug_id, purchase_date, expiration_date, quantity, remaining_quantity, unit_price, supplier)\n" +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        jdbc.update(sql,
                request.getDrugId(),
                request.getPurchaseDate(),
                request.getExpirationDate(),
                request.getQuantity(),
                request.getQuantity(), // 최초에는 남은 재고 = 입고 수량
                request.getUnitPrice(),
                request.getSupplier());

        String lastSql = "SELECT * FROM purchase ORDER BY purchase_id DESC LIMIT 1";
        Map<String, Object> latest = jdbc.queryForMap(lastSql);

        return ResponseEntity.ok(latest);
    }
}
